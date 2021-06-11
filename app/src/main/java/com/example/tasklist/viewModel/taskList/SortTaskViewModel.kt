package com.example.tasklist.viewModel.taskList

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.tasklist.dev.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel

class SortTaskViewModel: ViewModel() {
	val onCompletedTask = SingleLiveEvent<Unit>()
	val onSortTask = SingleLiveEvent<Unit>()
	val onDeletedTask = SingleLiveEvent<Unit>()

	val completedTaskClickListener = View.OnClickListener {
		onCompletedTask.call()
	}
	val sortTaskClickListener = View.OnClickListener {
		onSortTask.call()
	}
	val deletedTaskClickListener = View.OnClickListener {
		onDeletedTask.call()
	}
}