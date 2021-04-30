package com.example.tasklist.domain

import com.example.tasklist.api.model.response.TaskList
import com.example.tasklist.api.service.TaskListsApi
import com.example.tasklist.db.dao.TaskListDao
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

interface TaskListRepository {
	fun fetchTaskLists(): Completable
	fun getTaskList(): Flowable<List<TaskList>>
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

	override fun getTaskList(): Flowable<List<TaskList>> {
		return taskListDao.getAll()
	}
}