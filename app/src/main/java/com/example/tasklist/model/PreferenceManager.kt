package com.example.tasklist.model

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface PreferenceManager {
	var sharedPref: SharedPreferences
	var getToken: String?
	fun setToken(userToken: String)
}

class PreferenceManagerImpl @Inject constructor(@ApplicationContext context: Context)
	: PreferenceManager  {

	override var sharedPref: SharedPreferences = context
		.getSharedPreferences("TOKEN_PREFERENCES", Context.MODE_PRIVATE)

	override var getToken: String? = sharedPref.getString("USER_TOKEN", null)

	override fun setToken(userToken: String) {
		with (sharedPref.edit()) {
			putString("USER_TOKEN", userToken)
			apply()
		}
	}
}