package com.example.tasklist.model

import android.content.Context
import android.content.SharedPreferences
import com.example.tasklist.view.SignInFragment
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

interface PreferenceManager {
	var sharedPref: SharedPreferences
	var getToken: String
	fun setToken(userToken: String)
}

@InstallIn(SignInFragment::class)
@Module
class PreferenceManagerImpl @Inject constructor(@ActivityContext private val context: Context)
	: PreferenceManager  {

	override var sharedPref: SharedPreferences = context
		.getSharedPreferences("TOKEN_PREFERENCES", Context.MODE_PRIVATE)

	override var getToken: String = sharedPref.getString("USER_TOKEN", null).toString()

	override fun setToken(userToken: String) {
		with (sharedPref.edit()) {
			putString("USER_TOKEN", userToken)
			apply()
		}
	}
}