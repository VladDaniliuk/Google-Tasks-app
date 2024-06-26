package com.example.tasklist.api.model.response

import com.google.gson.annotations.SerializedName

data class AccessTokenResponse(
	@SerializedName("access_token")
	val accessToken: String,
	@SerializedName("expires_in")
	val expiresIn: Int,
	@SerializedName("token_type")
	val tokenType: String,
	@SerializedName("scope")
	val scope: String,
	@SerializedName("refresh_token")
	val refreshToken: String
)