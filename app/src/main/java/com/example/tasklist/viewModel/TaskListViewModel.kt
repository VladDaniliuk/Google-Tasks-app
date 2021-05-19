package com.example.tasklist.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasklist.BR
import com.example.tasklist.R
import com.example.tasklist.databinding.LayoutTaskBinding
import com.example.tasklist.dev.SimpleTaskClickListener
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.domain.TaskListRepository
import com.example.tasklist.domain.TaskRepository
import com.example.tasklist.view.adapter.BaseItemAdapter
import com.example.tasklist.view.itemModel.TaskItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
	private val taskRepository: TaskRepository,
	private val taskListRepository: TaskListRepository
) : ViewModel() {

	val fetchInProgress = MutableLiveData(false)

	private val _list = MutableLiveData<List<TaskItemModel>>()
	val list: LiveData<List<TaskItemModel>> = _list
	val listName = MutableLiveData<String>()

	private val onCreateTaskClick = SingleLiveEvent<Unit>()
	val onItemAdapter = SingleLiveEvent<String>()
	val onTaskClick = SingleLiveEvent<String>()

	val createTaskClickListener = View.OnClickListener {
		onCreateTaskClick.call()
	}

	val adapter = BaseItemAdapter<TaskItemModel, LayoutTaskBinding>(
		BR.model,
		R.layout.layout_task
	)

	var parentId: String? = null
		set(value) {
			field = value
			field?.let {
				getTaskList(it)
				getTask(it)
				fetchTasks(it)
			}
		}

	private fun getTaskList(parentId: String) {
		taskListRepository.getTaskList(parentId).subscribeOn(Schedulers.io()).subscribe {
			listName.postValue(it.title)
		}
	}

	private fun getTask(parentId: String) {
		taskRepository.getTask(parentId)
			.observeOn(AndroidSchedulers.mainThread())
			.map { list ->
				list.map { task ->
					TaskItemModel(
						task.task.id,
						task.task.parentId!!,
						task.task.title,
						task.task.status,
						task.task.due,
						object : SimpleTaskClickListener() {
							override fun onExpandItemClick(model: TaskItemModel) {
								_list.value?.let { currentList ->
									_list.postValue(currentList.map { taskItemModel ->
										taskItemModel.copy(
											subTaskVisibility =
											if (taskItemModel.id == model.id && taskItemModel
													.subTaskVisibility == View.GONE
											)
												View.VISIBLE
											else
												View.GONE
										)

									})
								}
							}

							override fun onTaskExecuteClick(model: TaskItemModel) {
								taskRepository.completeTask(model)
									.subscribeOn(Schedulers.computation())
									.subscribe()
							}
						},
						task.subTasks.map {
							TaskItemModel(
								it.id,
								it.parentId!!,
								it.title,
								it.status,
								it.due,
								object : SimpleTaskClickListener() {
									override fun onTaskExecuteClick(model: TaskItemModel) {
										taskRepository.completeTask(model)
											.subscribeOn(Schedulers.computation())
											.subscribe()
									}
								}
							)
						}
					)

				}
			}
			.subscribe {
				_list.postValue(it)
			}
	}

	fun fetchTasks(parentId: String) {
		fetchInProgress.postValue(true)
		taskRepository.fetchTasks(parentId).subscribeOn(Schedulers.io()).onErrorComplete()
			.doFinally {
				fetchInProgress.postValue(false)
			}.subscribe()
	}
}