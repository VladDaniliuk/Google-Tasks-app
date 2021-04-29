package com.example.tasklist.domain

import com.example.tasklist.api.model.body.GoogleAuthTokenBody
import com.example.tasklist.api.service.SignInApi
import com.example.tasklist.model.PreferenceManager
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

interface LogInRepository {
	fun onLogIn(
		clientId: String,
		secret: String,
		authCode: String?,
		authorizationCode: String,
		link: String
	): Completable
}

class LogInRepositoryImpl @Inject constructor(
	private val signInApi: SignInApi,
	private val preferenceManager: PreferenceManager
) : LogInRepository {
	override fun onLogIn(
		clientId: String, secret: String, authCode: String?,
		authorizationCode: String, link: String
	): Completable {
		return signInApi.getToken(
			GoogleAuthTokenBody(
				clientId,
				secret,
				authCode,
				authorizationCode,
				link
			)
		)
			.flatMapCompletable { m ->
				preferenceManager.setToken(m)
				Completable.complete()
			}
	}
}