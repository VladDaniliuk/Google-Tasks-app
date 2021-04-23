package com.example.tasklist.di

import com.example.tasklist.api.service.SignInApi
import com.example.tasklist.api.service.TaskListsApi
import com.example.tasklist.api.service.TasksApi
import com.example.tasklist.model.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

	private fun provideInterceptor(
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
	}

	@Provides
	@Singleton
	fun provideRetrofit(preferenceManager: PreferenceManager): Retrofit {
		return Retrofit.Builder().baseUrl(URL).client(
			OkHttpClient.Builder().addInterceptor(
				HttpLoggingInterceptor().setLevel(
					HttpLoggingInterceptor.Level.BODY
				)
			).addInterceptor { chain ->
				provideInterceptor(preferenceManager, chain)
			}
				.build()
		)
			.addCallAdapterFactory(RxJava3CallAdapterFactory.create()).addConverterFactory(
				GsonConverterFactory.create()
			).build()
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
	fun provideSignInApi(retrofit: Retrofit): SignInApi {
		return retrofit.create(SignInApi::class.java)
	}

	companion object {
		const val URL = "https://tasks.googleapis.com"
	}
}