package com.example.tasklist.model

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.tasklist.api.model.response.AccessTokenResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface PreferenceManager {
	val sharedPref: SharedPreferences
	val getToken: String?
	val getTokenType: String?
	fun setToken(userToken: AccessTokenResponse)
}

class PreferenceManagerImpl @Inject constructor(@ApplicationContext context: Context) :
	PreferenceManager {

	override val sharedPref: SharedPreferences = context
		.getSharedPreferences("TOKEN_PREFERENCES", Context.MODE_PRIVATE)

	override val getToken: String? = sharedPref.getString("USER_TOKEN", null)

	override val getTokenType: String? = sharedPref.getString("USER_TOKEN_TYPE", null)

	override fun setToken(userToken: AccessTokenResponse) {
		sharedPref.edit(true) {
			putString("USER_TOKEN", userToken.accessToken)
			putString("USER_REFRESH_TOKEN", userToken.refreshToken)
			putString("USER_TOKEN_TYPE", userToken.tokenType)
		}
	}
}