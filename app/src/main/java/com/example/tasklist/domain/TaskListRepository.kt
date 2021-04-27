package com.example.tasklist.domain

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.room.Room
import com.example.tasklist.api.service.TaskListsApi
import com.example.tasklist.db.dao.TaskListDao
import com.example.tasklist.di.AppDatabase
import com.example.tasklist.view.itemModel.TaskListItemModel
import com.google.api.services.tasks.model.TaskList
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface TaskListRepository {
	fun onTaskListsUpload(context: Context): Boolean
	fun getTaskList(): Single<List<TaskListItemModel>>
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
		return taskListsApi.getAllTaskLists()
			.flatMap { m ->
				taskListDao.insertAllTaskLists(m.items)
				Single.just(m)
			}
			.map { m ->
			m.items.map { TaskListItemModel(it.id, it.title) }
		}
	}
}