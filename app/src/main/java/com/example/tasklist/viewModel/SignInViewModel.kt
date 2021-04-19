package com.example.tasklist.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.model.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val preferenceManager: PreferenceManager) : ViewModel() {

	val onSignInClick = SingleLiveEvent<Unit>()

	val signInOnClickListener = View.OnClickListener {
		onSignInClick.call()
	}

	var progressBarVisibility = MutableLiveData(true)
	var buttonVisibility = MutableLiveData(false)

	fun setToken(string: String){
		preferenceManager.setToken(string)
	}

	val getToken: String? = preferenceManager.getToken

	fun onStartSignIn() {
		progressBarVisibility.postValue(true)
		buttonVisibility.postValue(false)
	}

	fun onCancelSignIn() {
		progressBarVisibility.postValue(false)
		buttonVisibility.postValue(true)
	}
}