package com.example.tasklist.domain

import com.example.tasklist.api.model.response.Task
import com.example.tasklist.api.service.TasksApi
import com.example.tasklist.db.dao.TaskDao
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

interface TaskRepository {
	fun fetchTasks(taskListId: String): Completable
	fun getTask(parentId: String): Flowable<List<Task>>
}

class TaskRepositoryImpl @Inject constructor(
	private val tasksApi: TasksApi,
	private val taskDao: TaskDao
) : TaskRepository {
	override fun fetchTasks(taskListId: String): Completable {
		return tasksApi.getAllTasks(taskListId).flatMapCompletable { baseListResponse ->
			baseListResponse.items.forEach {
				it.parentId = taskListId
			}
			taskDao.insertAllTasks(baseListResponse.items)
		}
	}

	override fun getTask(parentId: String): Flowable<List<Task>> {
		return taskDao.getAll(parentId).flatMap {list ->
			Flowable.fromIterable(list).filter {task->
				task.parent == null
			}.toList().toFlowable()
		}
	}
}