package com.example.tasklist.viewModel

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.domain.TaskListRepository
import com.example.tasklist.view.itemModel.TaskListItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TaskListListViewModel @Inject constructor(
	private val taskListRepository: TaskListRepository
) : ViewModel() {

	var list = MutableLiveData<List<TaskListItemModel>>()

	val onCreateTaskListClick = SingleLiveEvent<Unit>()

	val createTaskListClickListener = View.OnClickListener {
		onCreateTaskListClick.call()
	}

	fun checkInternet(context: Context) {
		if (taskListRepository.onTaskListsUpload(context)) {
			Log.d("INTERNET", "true")

			taskListRepository.getTaskList()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe({
					list.postValue(it)
				}, { Log.d("GET", "false") })
		} else {
			Log.d("INTERNET", "false")
		}
	}
}
