package com.example.tasklist.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.domain.TaskListRepository
import com.example.tasklist.view.itemModel.TaskListItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TaskListListViewModel @Inject constructor(
	private val taskListRepository: TaskListRepository
) : ViewModel() {

	private val _list = MutableLiveData<List<TaskListItemModel>>()
	val list: LiveData<List<TaskListItemModel>> = _list

	private val onCreateTaskListClick = SingleLiveEvent<Unit>()

	val createTaskListClickListener = View.OnClickListener {
		onCreateTaskListClick.call()
	}

	init {
		taskListRepository.getTaskList()
			.subscribeOn(Schedulers.computation())
			.subscribe {
				_list.postValue(it)
			}

		fetchTaskLists()
	}

	private fun fetchTaskLists() {
		taskListRepository.fetchTaskLists()
			.subscribeOn(Schedulers.io())
			.onErrorComplete()
			.subscribe()
	}
}
