package com.example.tasklist.viewModel

import com.example.tasklist.domain.TaskListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CreateTaskListViewModel @Inject constructor(
	private val taskListRepository: TaskListRepository
) : CreateBaseViewModel() {
	override fun onCreateBaseClick() {
		isClicked.postValue(true)
		taskListRepository.createTaskList(baseName.value.orEmpty())
			.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
			.subscribe({
				onCreateBaseFinish.call()
			}, {
				isClicked.postValue(false)
				onCreateBaseError.call()
			})
	}

}