package com.example.tasklist.domain

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.example.tasklist.api.model.response.TaskList
import com.example.tasklist.api.service.TaskListsApi
import com.example.tasklist.db.dao.TaskListDao
import com.example.tasklist.view.itemModel.TaskListItemModel
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface TaskListRepository {
	fun onTaskListsUpload(context: Context): Boolean
	fun getTaskList(): Single<List<TaskListItemModel>>
	fun getTaskListOffline(): Single<List<TaskListItemModel>>
}

class TaskListRepositoryImpl @Inject constructor(
	private val taskListsApi: TaskListsApi,
	private val taskListDao: TaskListDao
) : TaskListRepository {
	override fun onTaskListsUpload(context: Context): Boolean {
		val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
		return activeNetwork?.isConnectedOrConnecting == true
	}

	override fun getTaskList(): Single<List<TaskListItemModel>> {
		lateinit var list: List<TaskList>
		return taskListsApi.getAllTaskLists().doOnSuccess {
			list = it.items
		}
			.flatMapCompletable {
				taskListDao.insertAllTaskLists(it.items)
			}
			.toSingle {
				list
			}
			.map { m ->
				m.map { TaskListItemModel(it.id, it.title) }
			}
	}

	override fun getTaskListOffline(): Single<List<TaskListItemModel>> {
		return taskListDao.getAll().map { m ->
			m.map {
				Log.d("GET", TaskListItemModel(it.id, it.title).title)
				TaskListItemModel(it.id, it.title)
			}
		}
	}
}