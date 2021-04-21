package com.example.tasklist.model

import com.example.tasklist.api.service.TasksApi
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


interface RetrofitF {
	var URL: String
	var retrofit: Retrofit
	val retrofitService: TasksApi
	val logging: HttpLoggingInterceptor
	val client: OkHttpClient
}

class RetrofitImpl @Inject constructor() : RetrofitF {
	override var URL = "https://tasks.googleapis.com"
	override var retrofit = Retrofit.Builder().baseUrl(URL).build()

	override val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
	override val client = OkHttpClient.Builder().addInterceptor(logging).build()


	override val retrofitService: TasksApi
		get() = Retrofit.Builder()
			.baseUrl(URL).client(client)
			.addCallAdapterFactory(RxJava3CallAdapterFactory.create())
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create(TasksApi::class.java)
}

