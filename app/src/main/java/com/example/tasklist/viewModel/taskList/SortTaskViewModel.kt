package com.example.tasklist.viewModel.taskList

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasklist.R
import com.example.tasklist.dev.SingleLiveEvent

class SortTaskViewModel : ViewModel() {
	val postSetting = MutableLiveData<Triple<String, Int, String>>()
	val setting = MutableLiveData<Triple<String, Int, String>>()

	val onRadioButtonChoose = SingleLiveEvent<Int>()

	val onRadioButtonPost: (Int) -> Unit = {
		onRadioButtonChoose.postValue(it)
	}

	val completedTaskClickListener = View.OnClickListener {
		setting.value?.let { setting ->
			postSetting.postValue(
				Triple(
					when (setting.first) {
						it.resources.getString(R.string.show) -> it.resources
							.getString(R.string.hide)
						else -> it.resources.getString(R.string.show)
					},
					setting.second,
					setting.third
				)
			)
		}

	}

	val deletedTaskClickListener = View.OnClickListener {
		setting.value?.let { setting ->
			postSetting.postValue(
				when (setting.third) {
					it.resources.getString(R.string.assigned) -> {
						when (setting.second) {
							R.id.my_order -> setting.copy(
								third = it.resources.getString(R.string.deleted_small),
								second = R.id.date_to_add
							)
							else -> setting.copy(
								third = it.resources.getString(R.string.deleted_small)
							)
						}
					}
					else -> setting.copy(third = it.resources.getString(R.string.assigned))
				}
			)
		}
	}
}