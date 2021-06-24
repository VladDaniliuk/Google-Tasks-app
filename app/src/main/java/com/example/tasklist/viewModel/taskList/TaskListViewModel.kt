package com.example.tasklist.viewModel.taskList

import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasklist.BR
import com.example.tasklist.R
import com.example.tasklist.databinding.LayoutTaskBinding
import com.example.tasklist.dev.SimpleTaskClickListener
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.domain.TaskListRepository
import com.example.tasklist.domain.TaskRepository
import com.example.tasklist.view.adapter.BaseItemAdapter
import com.example.tasklist.view.itemModel.TaskItemModel
import com.example.tasklist.viewModel.baseViewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
	private val taskRepository: TaskRepository,
	private val taskListRepository: TaskListRepository
) : BaseViewModel() {
	private var getTaskDisposable: Disposable? = null

	private val _list = MutableLiveData<List<TaskItemModel>>()
	val list: LiveData<List<TaskItemModel>> = _list
	val listName = MutableLiveData<String>()
	val setting = MutableLiveData<Triple<String, String, String>>()

	val onExecuteTaskResult = SingleLiveEvent<String>()
	val onTaskListDelete = SingleLiveEvent<Unit>()
	val onTaskListEdit = SingleLiveEvent<Unit>()
	val onTaskSort = SingleLiveEvent<Unit>()

	val onMenuItemClickListener = Toolbar.OnMenuItemClickListener {
		onItemClicked(it)
		return@OnMenuItemClickListener true
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
				getTasks(it)
				fetchBase()
			}
		}

	private fun getTaskList(parentId: String) {
		taskListRepository.getTaskList(parentId).subscribeOn(Schedulers.io()).subscribe {
			listName.postValue(it.title)
		}
	}

	private fun getTasks(parentId: String) {
		getTaskDisposable?.dispose()
		getTaskDisposable = taskRepository.getTasks(parentId, setting.value)
			.observeOn(AndroidSchedulers.mainThread())
			.map { list ->
				list.map { task ->
					TaskItemModel(
						task.task.id,
						task.task.parentId!!,
						task.task.title!!,
						task.task.status!!,
						task.task.due,
						task.task.notes,
						task.task.deleted,
						object : SimpleTaskClickListener() {
							override fun onTaskItemClick(model: TaskItemModel, view: View) {
								onBaseClick.postValue(model.id to view)
							}

							override fun onExpandItemClick(model: TaskItemModel, view: View) {
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

							override fun onTaskExecuteClick(model: TaskItemModel, view: View) {
								taskRepository.completeTask(model)
									.subscribeOn(Schedulers.computation())
									.subscribe({}, { onExecuteTaskResult.postValue(model.title) })
							}
						},
						task.subTasks.map {
							TaskItemModel(
								it.id,
								it.parentId!!,
								it.title!!,
								it.status!!,
								it.due,
								it.notes,
								it.deleted,
								object : SimpleTaskClickListener() {
									override fun onTaskExecuteClick(
										model: TaskItemModel,
										view: View
									) {
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

	override fun fetchBase() {
		fetchInProgress.postValue(true)
		parentId?.let {
			taskRepository.fetchTasks(it)
				.subscribeOn(Schedulers.io())
				.onErrorComplete()
				.doFinally {
					fetchInProgress.postValue(false)
				}.subscribe()
		}
	}

	override fun deleteBase(id: String, forDelete: Boolean) {
		setTaskClickable(id, false)
		taskRepository.onDeleteTask(parentId!!, id, forDelete)
			.subscribeOn(Schedulers.io())
			.subscribe({
				onDeleteBaseResult.postValue(true)
			}, {
				setTaskClickable(id, true)
				onDeleteBaseResult.postValue(false)
			})
	}

	fun onItemClicked(menuItem: MenuItem) {
		when (menuItem.itemId) {
			R.id.delete -> onTaskListDelete.call()
			R.id.edit -> onTaskListEdit.call()
			R.id.sort -> onTaskSort.call()
		}
	}

	/*
	* Filter all tasks by id
	* So make it clickable/unclickable
	*/
	private fun setTaskClickable(id: String, clickable: Boolean) {
		list.value?.find {
			it.id == id
		}?.clickable?.postValue(clickable)
	}
}