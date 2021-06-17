package com.example.tasklist.viewModel.taskList

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasklist.dev.SingleLiveEvent

class SortTaskViewModel : ViewModel() {
	val postSetting = MutableLiveData<Triple<String, String, String>>()
	val setting = MutableLiveData<Triple<String, String, String>>()

	val onDeletedTask = SingleLiveEvent<Unit>()
	val onRadioButtonChoose = SingleLiveEvent<Int>()

	val completedTaskClickListener = View.OnClickListener {
		setting.value?.let {
			postSetting.postValue(
				Triple(
					when (it.first) {
						show -> hide
						else -> show
					},
					it.second,
					it.third
				)
			)
		}

	}

	val deletedTaskClickListener = View.OnClickListener {
		setting.value?.let {
			postSetting.postValue(
				Triple(
					it.first, it.second,
					when (it.third) {
						assigned -> deleted
						else -> assigned
					}
				)
			)
		}
	}

	companion object {
		const val show = "Show"
		const val hide = "Hide"
		const val add = "add"
		const val complete = "complete"
		const val deleted = "deleted"
		const val assigned = "assigned"
	}
}