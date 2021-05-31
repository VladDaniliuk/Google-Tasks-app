package com.example.tasklist.viewModel.task

import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ConcatAdapter
import com.example.tasklist.BR
import com.example.tasklist.R
import com.example.tasklist.databinding.LayoutSubTaskBinding
import com.example.tasklist.dev.SimpleTaskClickListener
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.domain.TaskRepository
import com.example.tasklist.view.adapter.AddSubTaskAdapter
import com.example.tasklist.view.adapter.BaseItemAdapter
import com.example.tasklist.view.adapter.TaskAdapter
import com.example.tasklist.view.itemModel.TaskItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskRepository: TaskRepository) : ViewModel() {
	var task = MutableLiveData<TaskItemModel>()

	val fetchInProgress = MutableLiveData(false)

	val onAddSubTaskClick = SingleLiveEvent<Unit>()
	val onCompleteTaskClick = SingleLiveEvent<Unit>()
	val onCompleteTaskError = SingleLiveEvent<String>()
	val onDeleteBaseClick = SingleLiveEvent<String>()
	val onDeleteBaseResult = SingleLiveEvent<Triple<String, Boolean, Boolean>>()
	val onTaskClick = SingleLiveEvent<String>()
	val onTaskDelete = SingleLiveEvent<Unit>()
	val onTaskEdit = SingleLiveEvent<Unit>()

	private var addSubTaskAdapter = AddSubTaskAdapter {
		onAddSubTaskClick.call()
	}
	private var taskControlsAdapter = TaskAdapter()
	val taskAdapter = BaseItemAdapter<TaskItemModel, LayoutSubTaskBinding>(
		BR.model,
		R.layout.layout_sub_task
	)
	val adapter = ConcatAdapter().apply {
		this.addAdapter(taskControlsAdapter)
		this.addAdapter(taskAdapter)
		this.addAdapter(addSubTaskAdapter)
	}

	val completeTaskClickListener = View.OnClickListener {
		onCompleteTaskClick.call()
	}
	val onMenuItemClickListener = Toolbar.OnMenuItemClickListener {
		onItemClicked(it)
		return@OnMenuItemClickListener true
	}

	var id: Pair<String, String>? = null
		set(value) {
			field = value
			field?.let {
				fetchTask()
			}
		}

	fun fetchTask() {
		fetchInProgress.postValue(true)
		id?.let {
			taskRepository.fetchTasks(it.first)
				.subscribeOn(Schedulers.io()).onErrorComplete()
				.subscribe {
					fetchInProgress.postValue(false)
					getTask(it.first, it.second)
				}
		}
	}

	private fun getTask(parentId: String, taskId: String) {
		taskRepository.getTask(parentId, taskId)
			.observeOn(AndroidSchedulers.mainThread())
			.map { taskWithSubTasks ->
				TaskItemModel(
					taskWithSubTasks.task.id,
					taskWithSubTasks.task.parentId!!,
					taskWithSubTasks.task.title!!,
					taskWithSubTasks.task.status!!,
					taskWithSubTasks.task.due,
					taskWithSubTasks.task.notes,
					null,
					taskWithSubTasks.subTasks.map {
						TaskItemModel(
							it.id,
							it.parentId!!,
							it.title!!,
							it.status!!,
							it.due,
							it.notes,
							object : SimpleTaskClickListener() {
								override fun onTaskExecuteClick(model: TaskItemModel) {
									taskRepository.completeTask(model)
										.subscribeOn(Schedulers.computation()).subscribe()
								}

								override fun onTaskItemClick(model: TaskItemModel) {
									onTaskClick.postValue(model.id)
								}
							}
						)
					}
				)
			}.subscribe {
				task.postValue(it)
				taskControlsAdapter.taskItemModel = it
				addSubTaskAdapter.taskItemModel = it
			}
	}

	fun onCompleteTaskClick() {
		taskRepository.completeTask(task.value!!)
			.subscribeOn(Schedulers.computation())
			.subscribe({
				fetchTask()
			}, {
				onCompleteTaskError.postValue(task.value!!.title)
			})
	}

	fun onItemClicked(menuItem: MenuItem) {
		if (menuItem.itemId == R.id.delete) {
			onTaskDelete.call()
		} else if (menuItem.itemId == R.id.edit) {
			onTaskEdit.call()
		}
	}

	fun deleteSubTask(id: String, forDelete: Boolean) {
		if (forDelete) {
			task.value?.list?.filter {
				it.id == id
			}?.toList()?.get(0)?.clickable?.postValue(false)
		}
		taskRepository.onDeleteTask(task.value?.parentId!!, id, forDelete)
			.subscribeOn(Schedulers.io())
			.subscribe({
				onDeleteBaseResult.postValue(Triple(id, forDelete, true))
			}, {
				task.value?.list?.filter {
					it.id == id
				}?.toList()?.get(0)?.clickable?.postValue(true)
				onDeleteBaseResult.postValue(Triple(id, forDelete, false))
			})
	}

	companion object Strings {
		const val completed = "completed"
		const val needAction = "needsAction"
	}
}