package com.example.tasklist.di

import com.example.tasklist.api.service.SignInApi
import com.example.tasklist.api.service.SignInApiHolder
import com.example.tasklist.api.service.TaskListsApi
import com.example.tasklist.api.service.TasksApi
import com.example.tasklist.model.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

	@Singleton
	@Provides
	fun provideSignInApiHolder(): SignInApiHolder = SignInApiHolder()

	/*private fun provideInterceptor(
		preferenceManager: PreferenceManager,
		chain: Interceptor.Chain
	): Response {
		return preferenceManager.getToken?.let { token ->
			preferenceManager.getTokenType?.let { tokenType ->
				chain.proceed(
					chain.request().newBuilder()
						.addHeader("Authorization", "$tokenType $token").build()
				)
			} ?: run {
				chain.proceed(chain.request())
			}
		} ?: run {
			chain.proceed(chain.request())
		}
	}*/

	@Provides
	@Singleton
	fun provideRetrofit(
		tokenAuthenticator: TokenAuthenticator
	): Retrofit {
		return Retrofit.Builder().baseUrl(URL).client(
			OkHttpClient.Builder()/*.addInterceptor(
				HttpLoggingInterceptor().setLevel(
					HttpLoggingInterceptor.Level.BODY
				)
			)*/
				.authenticator(tokenAuthenticator)
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
	private val preferenceManager: PreferenceManager/*,
	private val signInApiHolder: SignInApiHolder,
	@ApplicationContext private val context: Context*/
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
//			val accessTokenResponse: AccessTokenResponse? = signInApiHolder.signInApi?.refreshToken(
//				GoogleRefreshToken(
//					context.getString(R.string.default_web_client_id),
//					context.getString(R.string.secret), "refresh_token", refreshToken
//				)
//			)?.blockingGet()


		}
	}
}