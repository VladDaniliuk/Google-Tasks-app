package com.example.tasklist.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasklist.dev.SingleLiveEvent

open class BaseViewModel : ViewModel() {
	val fetchInProgress = MutableLiveData(false)

	val onCreateBaseClick = SingleLiveEvent<Unit>()
	val onDeleteBaseClick = SingleLiveEvent<String>()
	val onBaseClick = SingleLiveEvent<String>()
	val onDeleteBaseResult = SingleLiveEvent<Triple<String, Boolean, Boolean>>()

	val createBaseClickListener = View.OnClickListener {
		onCreateBaseClick.call()
	}

	open fun fetchBase() {}

	open fun deleteBase(id: String,forDelete: Boolean = true) {}
}