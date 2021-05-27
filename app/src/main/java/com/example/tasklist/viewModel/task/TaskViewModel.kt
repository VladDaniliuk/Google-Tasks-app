package com.example.tasklist.viewModel.task

import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ConcatAdapter
import com.example.tasklist.BR
import com.example.tasklist.R
import com.example.tasklist.databinding.LayoutTaskBinding
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.domain.TaskRepository
import com.example.tasklist.view.adapter.AddSubTaskAdapter
import com.example.tasklist.view.adapter.BaseItemAdapter
import com.example.tasklist.view.adapter.TaskAdapter
import com.example.tasklist.view.itemModel.TaskItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskRepository: TaskRepository) : ViewModel() {
	var task = MutableLiveData<TaskItemModel>()
	val subTasks = MutableLiveData<List<TaskItemModel>>()
	val fetchInProgress = MutableLiveData(false)

	val onCompleteTaskClick = SingleLiveEvent<Unit>()
	val onCompleteTaskError = SingleLiveEvent<String>()
	val onTaskDelete = SingleLiveEvent<Unit>()
	val onTaskEdit = SingleLiveEvent<Unit>()

	private var addSubTaskAdapter = AddSubTaskAdapter()
	private var taskControlsAdapter = TaskAdapter()
	val taskAdapter = BaseItemAdapter<TaskItemModel, LayoutTaskBinding>(
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
				getTask(it.first, it.second)
				fetchTask()
			}
		}

	fun fetchTask() {
		fetchInProgress.postValue(true)
		id?.let {
			taskRepository.fetchTask(it.first, it.second)
				.subscribeOn(Schedulers.io())
				.onErrorComplete()
				.doFinally {
					fetchInProgress.postValue(false)
				}.subscribe()
		}
	}

	private fun getTask(parentId: String, taskId: String) {
		taskRepository.getTask(parentId, taskId).subscribeOn(Schedulers.io())
			.subscribe({ taskWithSubTasks ->
				val taskItemModel = TaskItemModel(
					taskWithSubTasks.task.id,
					taskWithSubTasks.task.parentId!!,
					taskWithSubTasks.task.title!!,
					taskWithSubTasks.task.status!!,
					taskWithSubTasks.task.due,
					taskWithSubTasks.task.notes,
					null
				)

				task.postValue(taskItemModel)

				taskControlsAdapter.taskItemModel = taskItemModel
				addSubTaskAdapter.taskItemModel = taskItemModel

				subTasks.postValue(taskWithSubTasks.subTasks.map {
					TaskItemModel(
						it.id,
						it.parentId!!,
						it.title!!,
						it.status!!,
						it.due,
						it.notes,
						null
					)
				})
			}, {})
	}

	fun onCompleteTaskClick() {
		taskRepository.completeTask(task.value!!)
			.subscribeOn(Schedulers.computation())
			.subscribe({
				task.postValue(
					task.value?.copy(
						status = if (task.value?.status == completed)
							needAction
						else
							completed
					)
				)
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

	companion object Strings {
		const val completed = "completed"
		const val needAction = "needsAction"
	}
}