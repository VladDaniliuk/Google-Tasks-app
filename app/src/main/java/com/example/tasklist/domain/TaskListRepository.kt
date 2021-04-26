package com.example.tasklist.domain

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.example.tasklist.api.service.TaskListsApi
import com.example.tasklist.model.PreferenceManager
import com.example.tasklist.view.itemModel.TaskListItemModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

interface TaskListRepository {
	fun onTaskListsUpload(context: Context): Boolean
	fun getTaskList(): Single<List<TaskListItemModel>>
	fun setTaskList()
}

class TaskListRepositoryImpl @Inject constructor(
	private val taskListsApi: TaskListsApi,
	private val preferenceManager: PreferenceManager
) : TaskListRepository {
	override fun onTaskListsUpload(context: Context): Boolean {
		val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
		return activeNetwork?.isConnectedOrConnecting == true
	}

	override fun getTaskList(): Single<List<TaskListItemModel>> {
		return taskListsApi.getAllTaskLists().map { m ->
			m.items.map { TaskListItemModel(it.id, it.title) }
		}
	}

	override fun setTaskList() {
		getTaskList().subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe({Log.d("SET", "true")}, { Log.d("SET", "false") })
	}
}