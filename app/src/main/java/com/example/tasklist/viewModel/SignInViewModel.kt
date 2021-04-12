package com.example.tasklist.viewModel

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.tasklist.dev.SingleLiveEvent

class SignInViewModel : ViewModel() {

	val onSignInClick = SingleLiveEvent<Unit>()

	val signInOnClickListener = View.OnClickListener { onSignInClick.call() }
}