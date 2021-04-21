package com.example.tasklist.model

import com.google.gson.annotations.SerializedName

data class GoogleAuthTokenBody(
	@SerializedName("client_id")
	val clientId: String,
	@SerializedName("client_secret")
	val clientSecret: String,
	@SerializedName("code")
	val code: String?,
	@SerializedName("grant_type")
	val grantType: String,
	@SerializedName("redirect_uri")
	val redirectUri: String
)