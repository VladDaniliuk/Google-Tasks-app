package com.example.tasklist.viewModel

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.tasklist.api.service.TaskListsApi
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.view.itemModel.TaskListItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TaskListListViewModel @Inject constructor(
	private val taskListsApi: TaskListsApi
) : ViewModel() {

	lateinit var list: List<TaskListItemModel>

	val onCreateTaskListClick = SingleLiveEvent<Unit>()

	val getTaskListListEvent = SingleLiveEvent<Unit>()

	val createTaskListClickListener = View.OnClickListener {
		onCreateTaskListClick.call()
	}


	fun getTaskListList() {
		taskListsApi.getALLTaskLists()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe({ v ->
				list = v.items.map { TaskListItemModel(it.id, it.title) }
				getTaskListListEvent.call()
			}, { err ->
				Log.d("TAG", "err: $err")
			})
	}
}
