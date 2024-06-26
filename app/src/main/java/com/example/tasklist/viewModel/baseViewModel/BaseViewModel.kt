package com.example.tasklist.viewModel.baseViewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasklist.dev.SingleLiveEvent

open class BaseViewModel : ViewModel() {
	val fetchInProgress = MutableLiveData(false)

	val onBaseClick = SingleLiveEvent<Pair<String, View>>()
	val onCreateBaseClick = SingleLiveEvent<Unit>()
	val onDeleteBaseClick = SingleLiveEvent<Pair<String,Boolean>>()
	val onDeleteBaseResult = SingleLiveEvent<Boolean>()

	val onDeleteBaseClickEvent: (Pair<String, Boolean>) -> Unit = {
		onDeleteBaseClick.postValue(it)
	}

	val createBaseClickListener = View.OnClickListener {
		onCreateBaseClick.call()
	}

	open fun fetchBase(withAnim: Boolean? = true) {}

	open fun deleteBase(id: String, forDelete: Boolean = true) {}
}