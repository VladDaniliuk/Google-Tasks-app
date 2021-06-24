package com.example.tasklist.viewModel.baseViewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasklist.dev.SingleLiveEvent

open class CreateBaseViewModel : ViewModel() {
	val baseName = MutableLiveData<String>()
	var isLoading = MutableLiveData(false)

	val onCreateBaseClick = SingleLiveEvent<Unit>()
	val onCreateBaseFinish = SingleLiveEvent<Unit>()
	val onCreateBaseError = SingleLiveEvent<Unit>()

	val createBaseClickListener = View.OnClickListener {
		onCreateBaseClick.call()
	}

	open fun onCreateBaseClick() {}
}