package com.example.tasklist.viewModel.task

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasklist.api.model.response.Task
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.domain.TaskListRepository
import com.example.tasklist.domain.TaskRepository
import com.example.tasklist.view.itemModel.TaskListItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class ChangeTaskViewModel @Inject constructor(
	taskListRepository: TaskListRepository,
	private val taskRepository: TaskRepository
) : ViewModel() {
	val taskListList = MutableLiveData<List<TaskListItemModel>>()
	val task = MutableLiveData<Task>()
	val taskName = MutableLiveData<String>()
	val isClicked = MutableLiveData(false)

	val onChangeTaskClick = SingleLiveEvent<Unit>()
	val onChangeTaskClickResult = SingleLiveEvent<Pair<Boolean, Boolean>>()

	var id: Pair<String, String>? = null
		set(value) {
			field = value
			field?.let {
				taskRepository.getTask(it.first, it.second).subscribeOn(Schedulers.computation())
					.subscribe { taskWithSubTasks ->
						task.postValue(taskWithSubTasks.task)
						taskName.postValue(taskWithSubTasks.task.title!!)
					}
			}
		}

	val changeTaskClickListener = View.OnClickListener {
		onChangeTaskClick.call()
	}

	init {
		taskListRepository.getTaskLists()
			.subscribeOn(Schedulers.computation())
			.map { m ->
				m.map { taskList ->
					TaskListItemModel(taskList.id, taskList.title)
				}
			}
			.subscribe {
				taskListList.postValue(it)
			}
	}

/*
*	Pair(First,Second)
*	First - Choice in rename/change tasklist
*	Second - Connection
* 	First : Rename - false | Change tasklist - true
*/

	fun changeTask(newTaskListId: String? = null) {
		isClicked.postValue(true)
		if (newTaskListId != null) {
			taskName.value?.let {
				taskRepository.createTask(newTaskListId, null, it, task.value?.due)
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe({
						taskRepository.onDeleteTask(id!!.first, id!!.second, true)
							.subscribeOn(Schedulers.computation()).subscribe {
								isClicked.postValue(false)
								onChangeTaskClickResult.postValue(Pair(first = true, second = true))
							}
					}, {
						isClicked.postValue(false)
						onChangeTaskClickResult.postValue(Pair(first = true, second = false))
					})
			}
		} else {
			taskRepository.changeTask(id!!.first, Task(id!!.second, taskName.value))
				.subscribeOn(Schedulers.io())
				.subscribe({
					isClicked.postValue(false)
					onChangeTaskClickResult.postValue(Pair(first = false, second = true))
				}, {
					isClicked.postValue(false)
					onChangeTaskClickResult.postValue(Pair(first = false, second = false))
				})
		}
	}
}