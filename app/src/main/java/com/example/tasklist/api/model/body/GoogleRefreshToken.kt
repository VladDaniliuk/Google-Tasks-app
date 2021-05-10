package com.example.tasklist.api.model.body

import com.google.gson.annotations.SerializedName

data class GoogleRefreshToken(
	@SerializedName("client_id")
	val clientId: String,
	@SerializedName("client_secret")
	val clientSecret: String,
	@SerializedName("grant_type")
	val grantType: String,
	@SerializedName("refresh_token")
	val refreshToken: String
)