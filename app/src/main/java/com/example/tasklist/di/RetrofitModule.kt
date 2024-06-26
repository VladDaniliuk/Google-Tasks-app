package com.example.tasklist.di

import android.content.Context
import com.example.tasklist.R
import com.example.tasklist.api.model.body.GoogleRefreshToken
import com.example.tasklist.api.service.SignInApi
import com.example.tasklist.api.service.SignInApiHolder
import com.example.tasklist.api.service.TaskListsApi
import com.example.tasklist.api.service.TasksApi
import com.example.tasklist.model.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

	@Singleton
	@Provides
	fun provideSignInApiHolder(): SignInApiHolder = SignInApiHolder()

	@Provides
	@Singleton
	fun provideRetrofit(
		tokenAuthenticator: TokenAuthenticator
	): Retrofit {
		return Retrofit.Builder().baseUrl(URL).client(
			OkHttpClient.Builder().addInterceptor(
				HttpLoggingInterceptor().setLevel(
					HttpLoggingInterceptor.Level.BODY
				)
			).authenticator(tokenAuthenticator)
				.build()
		).addCallAdapterFactory(RxJava3CallAdapterFactory.create())
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}

	@Provides
	@Singleton
	fun provideTasksApi(retrofit: Retrofit): TasksApi {
		return retrofit.create(TasksApi::class.java)
	}

	@Provides
	@Singleton
	fun provideTaskListsApi(retrofit: Retrofit): TaskListsApi {
		return retrofit.create(TaskListsApi::class.java)
	}

	@Provides
	@Singleton
	fun provideSignInApi(retrofit: Retrofit, signInApiHolder: SignInApiHolder): SignInApi {
		return retrofit.create(SignInApi::class.java).apply {
			signInApiHolder.signInApi = this
		}
	}

	companion object {
		const val URL = "https://tasks.googleapis.com"
	}
}

class TokenAuthenticator @Inject constructor(
	private val preferenceManager: PreferenceManager,
	private val signInApiHolder: SignInApiHolder,
	@ApplicationContext private val context: Context
) : Authenticator {
	override fun authenticate(route: Route?, response: Response): Request? {
		if (response.request.header("Authorization") != null) {
			try {
				val refreshToken = signInApiHolder.signInApi!!.refreshToken(
					GoogleRefreshToken(
						context.getString(R.string.default_web_client_id),
						context.getString(R.string.secret),
						"refresh_token",
						preferenceManager.getRefreshToken!!
					)
				).blockingGet()
				preferenceManager.setUserToken(refreshToken.accessToken)
				return preferenceManager.getTokenType?.let { tokenType ->
					response.request.newBuilder()
						.addHeader(
							"Authorization",
							"$tokenType ${refreshToken.accessToken}"
						).build()
				}
			} catch (e: Exception) {
				Timber.e(e)
				return null
			}
		}

		return preferenceManager.getToken?.let { token ->
			preferenceManager.getTokenType?.let { tokenType ->
				response.request.newBuilder().header(
					"Authorization",
					"$tokenType $token"
				).build()
			}
		}
	}
}