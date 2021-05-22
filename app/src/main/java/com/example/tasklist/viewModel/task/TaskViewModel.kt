package com.example.tasklist.viewModel.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.domain.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskRepository: TaskRepository) : ViewModel() {
	val taskName = MutableLiveData<String>()

	val onCompleteTaskClick = SingleLiveEvent<Unit>()
	val onCompleteTaskError = SingleLiveEvent<String>()

	var id: Pair<String, String>? = null
		set(value) {
			field = value
			field?.let {
				getTask(it.first, it.second)
			}
		}

	private fun getTask(parentId: String, taskId: String) {
		taskRepository.getTask(parentId, taskId).subscribeOn(Schedulers.io()).subscribe({
			taskName.postValue(it.title!!)
		}, {})
	}

	fun completeTask() {
	}
}