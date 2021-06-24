package com.example.tasklist.domain

import com.example.tasklist.api.model.response.TaskList
import com.example.tasklist.api.service.TaskListsApi
import com.example.tasklist.db.dao.TaskListDao
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

interface TaskListRepository {
	fun fetchTaskLists(): Completable
	fun getTaskLists(): Flowable<List<TaskList>>
	fun createTaskList(title: String): Completable
	fun getTaskList(id: String): Flowable<TaskList>
	fun deleteTaskList(id: String): Completable
	fun changeTaskList(id: String, newName: String): Completable
}

class TaskListRepositoryImpl @Inject constructor(
	private val taskListsApi: TaskListsApi,
	private val taskListDao: TaskListDao
) : TaskListRepository {
	override fun fetchTaskLists(): Completable {
		return taskListsApi.getAllTaskLists().flatMapCompletable {
			Completable.fromCallable { taskListDao.updateAllTaskLists(it.items) }
		}
	}

	override fun getTaskLists(): Flowable<List<TaskList>> {
		return taskListDao.getAll()
	}

	override fun createTaskList(title: String): Completable {
		return taskListsApi.insertTaskList(TaskList("", title)).flatMapCompletable {
			fetchTaskLists()
		}
	}

	override fun getTaskList(id: String): Flowable<TaskList> {
		return taskListDao.getTaskList(id)
	}

	override fun deleteTaskList(id: String): Completable {
		return taskListsApi.deleteTaskList(id).doOnComplete {
			taskListDao.delete(id).subscribeOn(Schedulers.io()).subscribe()
		}
	}

	override fun changeTaskList(id: String, newName: String): Completable {
		return taskListsApi.patchTaskList(id, TaskList(id, newName)).flatMapCompletable {
			fetchTaskLists()
		}
	}
}