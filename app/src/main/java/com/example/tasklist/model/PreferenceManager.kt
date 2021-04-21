package com.example.tasklist.model

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface PreferenceManager {
	val sharedPref: SharedPreferences
	val getToken: String?
	fun setToken(userToken: String)
}

class PreferenceManagerImpl @Inject constructor(@ApplicationContext context: Context) :
	PreferenceManager {

	override val sharedPref: SharedPreferences = context
		.getSharedPreferences("TOKEN_PREFERENCES", Context.MODE_PRIVATE)

	override val getToken: String? = sharedPref.getString("USER_TOKEN", null)

	override fun setToken(userToken: String) {
		with(sharedPref.edit()) {
			putString("USER_TOKEN", userToken)
			apply()
		}
	}
}