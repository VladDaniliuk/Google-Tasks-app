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

	var progressBarVisibility = MutableLiveData<Int>()
	var buttonVisibility = MutableLiveData<Int>()

	fun onStartSignIn() {
		progressBarVisibility.postValue(View.VISIBLE)
		buttonVisibility.postValue(View.GONE)
	}

	fun onCancelSignIn() {
		progressBarVisibility.postValue(View.GONE)
		buttonVisibility.postValue(View.VISIBLE)
	}
}