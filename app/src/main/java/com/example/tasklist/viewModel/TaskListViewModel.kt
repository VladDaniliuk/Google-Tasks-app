package com.example.tasklist.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.domain.TaskRepository
import com.example.tasklist.view.itemModel.TaskItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
	private val taskRepository: TaskRepository
) : ViewModel() {

	val fetchInProgress = MutableLiveData(false)

	private val _list = MutableLiveData<List<TaskItemModel>>()
	val list: LiveData<List<TaskItemModel>> = _list

	private val onCreateTaskClick = SingleLiveEvent<Unit>()
	val onTaskClick = SingleLiveEvent<String>()

	val createTaskClickListener = View.OnClickListener {
		onCreateTaskClick.call()
	}

	var parentId : String? = null
	set(value) {
		field = value
		field?.let { getTask(it) }
	}


	private fun getTask(parentId: String) {
		taskRepository.getTask(parentId)
			.subscribeOn(Schedulers.computation())
			.map { m ->
				m.map { task ->
					TaskItemModel(task.id, task.title, task.status) {
						onTaskClick.postValue(it)
					}
				}
			}
			.subscribe {
				_list.postValue(it)
			}

		fetchTasks(parentId)
	}

	fun fetchTasks(parentId: String) {
		fetchInProgress.postValue(true)
		taskRepository.fetchTasks(parentId).subscribeOn(Schedulers.io()).onErrorComplete()
			.doFinally {
				fetchInProgress.postValue(false)
			}.subscribe()
	}
}