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

	private fun provideInterceptor(
		preferenceManager: PreferenceManager,
		chain: Interceptor.Chain,
		signInApiHolder: SignInApiHolder,
		context: Context
	): Response {
		return preferenceManager.getToken?.let { token ->
			preferenceManager.getTokenType?.let { tokenType ->
				val authResponse = chain.proceed(
					chain.request().newBuilder()
						.addHeader("Authorization", "$tokenType $token").build()
				)
				when (authResponse.code) {
					403, 401 -> {
						Timber.v("401/403")
						val refreshToken = signInApiHolder.signInApi!!.refreshToken(
							GoogleRefreshToken(
								context.getString(R.string.default_web_client_id),
								context.getString(R.string.secret),
								"refresh_token",
								preferenceManager.getRefreshToken!!
							)
						).blockingGet()
						preferenceManager.setUserToken(refreshToken.accessToken)
						chain.proceed(
							chain.request().newBuilder()
								.addHeader(
									"Authorization",
									"$tokenType ${refreshToken.accessToken}"
								).build()
						)
					}
					else -> {
						Timber.v(authResponse.code.toString())
						authResponse
					}
				}
			} ?: run {
				chain.proceed(chain.request())
			}
		} ?: run {
			chain.proceed(chain.request())
		}
	}

	@Provides
	@Singleton
	fun provideRetrofit(
		preferenceManager: PreferenceManager,
		signInApiHolder: SignInApiHolder,
		@ApplicationContext context: Context
	): Retrofit {
		return Retrofit.Builder().baseUrl(URL).client(
			OkHttpClient.Builder().addInterceptor(
				HttpLoggingInterceptor().setLevel(
					HttpLoggingInterceptor.Level.BODY
				)
			).addInterceptor { chain ->
				provideInterceptor(preferenceManager, chain, signInApiHolder, context)
			}
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
	private val preferenceManager: PreferenceManager
) : Authenticator {
	override fun authenticate(route: Route?, response: Response): Request? {
		if (response.request.header("Authorization") != null) {
			return null// Give up, we've already failed to authenticate.
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