package com.example.tasklist.model

import com.google.gson.annotations.SerializedName

data class AccessTokenBody(
	@SerializedName("access_token")
	val accessToken: String,
	@SerializedName("expires_in")
	val expiresIn: String,
	@SerializedName("token_type")
	val tokenType: String,
	@SerializedName("scope")
	val scope: String,
	@SerializedName("refresh_token")
	val refreshToken: String
)