package com.example.tasklist.viewModel.taskListList

import com.example.tasklist.domain.TaskListRepository
import com.example.tasklist.viewModel.baseViewModel.CreateBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CreateTaskListViewModel @Inject constructor(
	private val taskListRepository: TaskListRepository
) : CreateBaseViewModel() {

	override fun onCreateBaseClick() {
		isLoading.postValue(true)
		taskListRepository.createTaskList(baseName.value.orEmpty())
			.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
			.subscribe({
				onCreateBaseFinish.call()
			}, {
				isLoading.postValue(false)
				onCreateBaseError.call()
			})
	}
}