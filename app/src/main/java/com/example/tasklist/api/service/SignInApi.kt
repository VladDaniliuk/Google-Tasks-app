package com.example.tasklist.api.service

import com.example.tasklist.api.model.body.GoogleAuthTokenBody
import com.example.tasklist.api.model.body.GoogleRefreshToken
import com.example.tasklist.api.model.response.AccessTokenResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface SignInApi {
	@POST("https://oauth2.googleapis.com/token")
	fun getToken(@Body body: GoogleAuthTokenBody): Single<AccessTokenResponse>

	@POST("https://oauth2.googleapis.com/token")
	fun refreshToken(@Body body: GoogleRefreshToken): Single<AccessTokenResponse>
}