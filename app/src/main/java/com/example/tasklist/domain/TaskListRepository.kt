package com.example.tasklist.domain

import android.util.Log
import com.example.tasklist.api.service.TaskListsApi
import com.example.tasklist.db.dao.TaskListDao
import com.example.tasklist.view.itemModel.TaskListItemModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

interface TaskListRepository {
	fun fetchTaskLists(): Completable
	fun getTaskList(): Flowable<List<TaskListItemModel>>
}

class TaskListRepositoryImpl @Inject constructor(
	private val taskListsApi: TaskListsApi,
	private val taskListDao: TaskListDao
) : TaskListRepository {
	override fun fetchTaskLists(): Completable {
		return taskListsApi.getAllTaskLists().flatMapCompletable {
			taskListDao.insertAllTaskLists(it.items)
		}
	}

	override fun getTaskList(): Flowable<List<TaskListItemModel>> {
		return taskListDao.getAll().map { m ->
			m.map {
				Log.d("GET", TaskListItemModel(it.id, it.title).title)
				TaskListItemModel(it.id, it.title)
			}
		}
	}
}