package com.example.tasklist.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tasklist.api.model.response.TaskList
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.model.PreferenceManager
import com.example.tasklist.model.RetrofitF
import com.example.tasklist.view.itemModel.TaskListItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TaskListListViewModel @Inject constructor(
	private val retrofit: RetrofitF,
	private val preferenceManager: PreferenceManager
) : ViewModel() {

	lateinit var list: List<TaskListItemModel>

	val getTaskListListEvent = SingleLiveEvent<Unit>()

	fun getTaskListList() {
		retrofit.retrofitService.getALLTaskLists("Bearer ${preferenceManager.getToken}")
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
