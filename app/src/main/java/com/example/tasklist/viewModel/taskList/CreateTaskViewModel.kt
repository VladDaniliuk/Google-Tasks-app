package com.example.tasklist.viewModel.taskList

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.domain.TaskRepository
import com.example.tasklist.viewModel.baseViewModel.CreateBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
	private val taskRepository: TaskRepository
) : CreateBaseViewModel() {
	var taskListId: String? = null
	var taskParentId: String? = null

	var dueDate = MutableLiveData<String>()

	val setDateClick = SingleLiveEvent<Unit>()

	val setDateClickListener = View.OnClickListener {
		setDateClick.call()
	}

	override fun onCreateBaseClick() {
		isLoading.postValue(true)
		taskRepository.createTask(
			taskListId!!,
			taskParentId,
			baseName.value.orEmpty(),
			dueDate.value.orEmpty()
		).subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe({
				onCreateBaseFinish.call()
			}, {
				isLoading.postValue(false)
				onCreateBaseError.call()
			})
	}
}