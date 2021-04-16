package com.example.tasklist.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasklist.dev.SingleLiveEvent

class SignInViewModel : ViewModel() {


	val onSignInClick = SingleLiveEvent<Unit>()

	val signInOnClickListener = View.OnClickListener {
		onSignInClick.call()
	}

	var progressBarVisibility = MutableLiveData(true)
	var buttonVisibility = MutableLiveData(false)

	fun onStartSignIn() {
		progressBarVisibility.postValue(true)
		buttonVisibility.postValue(false)
	}

	fun onCancelSignIn() {
		progressBarVisibility.postValue(false)
		buttonVisibility.postValue(true)
	}
}