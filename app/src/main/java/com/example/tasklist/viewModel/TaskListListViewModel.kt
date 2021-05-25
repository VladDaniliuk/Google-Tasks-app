package com.example.tasklist.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasklist.BR
import com.example.tasklist.R
import com.example.tasklist.databinding.LayoutTaskListBinding
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.domain.TaskListRepository
import com.example.tasklist.view.adapter.BaseItemAdapter
import com.example.tasklist.view.itemModel.TaskListItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TaskListListViewModel @Inject constructor(
	private val taskListRepository: TaskListRepository
) : ViewModel() {

	val fetchInProgress = MutableLiveData(false)

	private val _list = MutableLiveData<List<TaskListItemModel>>()
	val list: LiveData<List<TaskListItemModel>> = _list

	val onCreateTaskListClick = SingleLiveEvent<Unit>()
	val onDeleteTaskListClick = SingleLiveEvent<String>()
	val onTaskListClick = SingleLiveEvent<String>()
	val onDeleteTaskListResult = SingleLiveEvent<Pair<String, Boolean>>()

	val createTaskListClickListener = View.OnClickListener {
		onCreateTaskListClick.call()
	}

	val adapter = BaseItemAdapter<TaskListItemModel, LayoutTaskListBinding>(
		BR.model,
		R.layout.layout_task_list
	)

	init {
		taskListRepository.getTaskLists()
			.subscribeOn(Schedulers.computation())
			.map { m ->
				m.map { taskList ->
					TaskListItemModel(taskList.id, taskList.title) {
						onTaskListClick.postValue(it)
					}
				}
			}
			.subscribe {
				_list.postValue(it)
			}

		fetchTaskLists()
	}

	fun fetchTaskLists() {
		fetchInProgress.postValue(true)
		taskListRepository.fetchTaskLists()
			.subscribeOn(Schedulers.io())
			.onErrorComplete()
			.doFinally {
				fetchInProgress.postValue(false)
			}
			.subscribe()
	}

	fun deleteTaskList(id: String) {
		onClickableTaskList(id, false)
		taskListRepository.deleteTaskList(id).subscribeOn(Schedulers.io())
			.subscribe({
				onDeleteTaskListResult.postValue(Pair(id, true))
			}, {
				onClickableTaskList(id, true)
				onDeleteTaskListResult.postValue(Pair(id, false))
			})
	}

	/*
	* Filter all taskLists by id, convert to list and get first element
	* In this situation we always have only one element in filtered list
	* So get first element of list(0) and make it clickable/unclickable
	*/
	private fun onClickableTaskList(id: String, clickable: Boolean) {
		list.value?.filter {
			it.id == id
		}?.toList()?.get(0)?.clickable = clickable
	}
}
