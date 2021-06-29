package com.example.tasklist.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.domain.LogInRepository
import com.example.tasklist.model.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
	preferenceManager: PreferenceManager,
	private val logInRepository: LogInRepository,
) : ViewModel() {

	val onSignInClick = SingleLiveEvent<Unit>()
	val onLoginSuccessEvent = SingleLiveEvent<Unit>()

	var progressBarVisibility = MutableLiveData(true)
	var buttonVisibility = MutableLiveData(false)

	val signInOnClickListener = View.OnClickListener {
		onSignInClick.call()
	}

	val getToken: String? = preferenceManager.getToken

	fun continueAuth(
		clientId: String, secret: String, authCode: String?, authorizationCode: String, link: String
	) {
		logInRepository.logIn(clientId, secret, authCode, authorizationCode, link)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe({
				onLoginSuccessEvent.call()
			}, {
				Timber.e(it)
			})
	}

	fun onStartSignIn() {
		progressBarVisibility.postValue(true)
		buttonVisibility.postValue(false)
	}

	fun onCancelSignIn() {
		progressBarVisibility.postValue(false)
		buttonVisibility.postValue(true)
	}
}