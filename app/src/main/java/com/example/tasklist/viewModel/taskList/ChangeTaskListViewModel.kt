package com.example.tasklist.viewModel.taskList

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.domain.TaskListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class ChangeTaskListViewModel @Inject constructor(
	private val taskListRepository: TaskListRepository
) : ViewModel() {

	val isClicked = MutableLiveData(false)
	val taskName = MutableLiveData<String>()

	val onChangeTaskListClick = SingleLiveEvent<Unit>()
	val onChangeTaskListFinish = SingleLiveEvent<Boolean>()

	var taskListId: String? = null
		set(value) {
			field = value
			field?.let { taskListId ->
				getTaskList(taskListId)
			}
		}

	val changeTaskListClickListener = View.OnClickListener {
		onChangeTaskListClick.call()
	}

	private fun getTaskList(taskListId: String) {
		taskListRepository.getTaskList(taskListId)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe {
				taskName.postValue(it.title)
			}
	}

	fun changeTaskListClick() {
		isClicked.postValue(true)
		taskListId?.let { taskListId ->
			taskName.value?.let { task ->
				taskListRepository.changeTaskList(taskListId, task).subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe({
						onChangeTaskListFinish.postValue(true)
					}, {
						isClicked.postValue(false)
						onChangeTaskListFinish.postValue(false)
					})
			}
		}
	}
}