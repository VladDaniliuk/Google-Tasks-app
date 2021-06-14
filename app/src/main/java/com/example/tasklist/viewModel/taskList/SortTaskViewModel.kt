package com.example.tasklist.viewModel.taskList

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasklist.dev.SingleLiveEvent

class SortTaskViewModel : ViewModel() {
	val postSetting = MutableLiveData<Triple<String, String, String>>()
	val setting = MutableLiveData<Triple<String, String, String>>()

	val onDeletedTask = SingleLiveEvent<Unit>()

	val completedTaskClickListener = View.OnClickListener {
		if (setting.value?.first ?: show == show)
			postSetting.postValue(
				Triple(
					hide,
					setting.value?.second ?: add,
					setting.value?.third ?: add
				)
			)
		else
			postSetting.postValue(
				Triple(
					show,
					setting.value?.second ?: add,
					setting.value?.third ?: add
				)
			)
	}
	val sortTaskClickListener = View.OnClickListener {
		if (setting.value?.second ?: add == add)
			postSetting.postValue(
				Triple(
					setting.value?.first ?: show,
					complete,
					setting.value?.third ?: add
				)
			)
		else
			postSetting.postValue(
				Triple(
					setting.value?.first ?: show,
					add,
					setting.value?.third ?: add
				)
			)
	}
	val deletedTaskClickListener = View.OnClickListener {
		if (setting.value?.third ?: assigned == assigned)
			postSetting.postValue(
				Triple(
					setting.value?.first ?: show,
					setting.value?.second ?: add,
					deleted
				)
			)
		else
			postSetting.postValue(
				Triple(
					setting.value?.first ?: show,
					setting.value?.second ?: add,
					assigned
				)
			)
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