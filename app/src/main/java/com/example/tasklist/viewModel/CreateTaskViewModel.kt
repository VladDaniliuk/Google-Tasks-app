package com.example.tasklist.viewModel

import com.example.tasklist.domain.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
	private val taskRepository: TaskRepository
) : CreateBaseViewModel() {
	var taskListId: String? = null

	override fun onCreateBaseClick() {
		isClicked.postValue(true)
		taskRepository.createTask(taskListId!!, baseName.value.orEmpty())
			.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
			.subscribe({
				onCreateBaseFinish.call()
			}, {
				isClicked.postValue(false)
				onCreateBaseError.call()
			})
	}
}