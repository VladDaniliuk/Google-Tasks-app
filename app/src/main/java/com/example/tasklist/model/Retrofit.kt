package com.example.tasklist.model

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


interface RetrofitF {
	var URL: String
	var retrofit: Retrofit
	val retrofitService: TasksApi
}

class RetrofitImpl @Inject constructor() : RetrofitF {
	override var URL = "https://tasks.googleapis.com"
	override var retrofit = Retrofit.Builder().baseUrl(URL).build()

	override val retrofitService: TasksApi
		get() = Retrofit.Builder()
			.baseUrl(URL)
			.addCallAdapterFactory(RxJava3CallAdapterFactory.create())
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create(TasksApi::class.java)
}

