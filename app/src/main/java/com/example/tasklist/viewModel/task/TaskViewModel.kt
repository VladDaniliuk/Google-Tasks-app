package com.example.tasklist.viewModel.task

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ConcatAdapter
import com.example.tasklist.BR
import com.example.tasklist.R
import com.example.tasklist.databinding.LayoutTaskBinding
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.domain.TaskRepository
import com.example.tasklist.view.adapter.BaseItemAdapter
import com.example.tasklist.view.adapter.TaskAdapter
import com.example.tasklist.view.itemModel.TaskItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskRepository: TaskRepository) : ViewModel() {
	val task = MutableLiveData<TaskItemModel>()
	val subTasks = MutableLiveData<List<TaskItemModel>>()

	private val taskControlsAdapter = TaskAdapter()
	val taskAdapter = BaseItemAdapter<TaskItemModel, LayoutTaskBinding>(
		BR.model,
		R.layout.layout_sub_task
	)
	val adapter = ConcatAdapter().apply {
		this.addAdapter(taskControlsAdapter)
		this.addAdapter(taskAdapter)
	}

	val onCompleteTaskClick = SingleLiveEvent<Unit>()
	val onCompleteTaskError = SingleLiveEvent<String>()

	val completeTaskClickListener = View.OnClickListener {
		onCompleteTaskClick.call()
	}

	var id: Pair<String, String>? = null
		set(value) {
			field = value
			field?.let {
				getTask(it.first, it.second)
			}
		}

	private fun getTask(parentId: String, taskId: String) {
		taskRepository.getTask(parentId, taskId).subscribeOn(Schedulers.io())
			.subscribe({ taskWithSubTasks ->
				task.postValue(
					TaskItemModel(
						taskWithSubTasks.task.id,
						taskWithSubTasks.task.parentId!!,
						taskWithSubTasks.task.title!!,
						taskWithSubTasks.task.status!!,
						taskWithSubTasks.task.due,
						taskWithSubTasks.task.notes,
						null
					)
				)

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

	fun completeTask() {
		taskRepository.completeTask(task.value!!).subscribeOn(Schedulers.computation())
			.subscribe({}, { onCompleteTaskError.postValue(task.value!!.title) })
	}
}