package com.example.tasklist.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.domain.TaskListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateTaskListViewModel @Inject constructor(
	private val taskListRepository: TaskListRepository
) : ViewModel() {

	val taskName = MutableLiveData("")
	val isClicked = MutableLiveData(false)

	val onCreateTaskListClick = SingleLiveEvent<Unit>()
	val onCreateTaskListFinish = SingleLiveEvent<Unit>()

	val createTaskListClickListener = View.OnClickListener {
		onCreateTaskListClick.call()
	}

	fun createTaskListClick() {
		isClicked.postValue(true)
		taskListRepository.createTaskList(taskName.value.orEmpty())
			.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
			.subscribe({
				onCreateTaskListFinish.call()
			}, {
				Timber.d(it)
			})
	}

}