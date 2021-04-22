package com.example.tasklist.viewModel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasklist.api.model.body.GoogleAuthTokenBody
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.model.PreferenceManager
import com.example.tasklist.model.RetrofitF
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
	private val preferenceManager: PreferenceManager,
	private val retrofit: RetrofitF
) : ViewModel() {

	val onSignInClick = SingleLiveEvent<Unit>()

	val loginEvent = SingleLiveEvent<Unit>()

	val signInOnClickListener = View.OnClickListener {
		onSignInClick.call()
	}

	var progressBarVisibility = MutableLiveData(true)
	var buttonVisibility = MutableLiveData(false)

	val getToken: String? = preferenceManager.getToken

	fun setToken(googleAuthTokenBody: GoogleAuthTokenBody) {
		retrofit.retrofitService.getToken(googleAuthTokenBody).subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread()).subscribe({ v ->
				preferenceManager.setToken(v)
				loginEvent.call()
			},
				{ Log.e("LOG", null, it) }
			)
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