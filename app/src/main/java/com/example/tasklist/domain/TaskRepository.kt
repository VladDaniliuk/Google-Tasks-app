package com.example.tasklist.domain

import com.example.tasklist.api.model.response.STATUS_COMPLETED
import com.example.tasklist.api.model.response.STATUS_NEEDS_ACTION
import com.example.tasklist.api.model.response.Task
import com.example.tasklist.api.model.response.TaskWithSubTasks
import com.example.tasklist.api.service.TasksApi
import com.example.tasklist.db.dao.TaskDao
import com.example.tasklist.view.itemModel.TaskItemModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

interface TaskRepository {
	fun fetchTasks(taskListId: String): Completable
	fun getTask(parentId: String): Flowable<List<TaskWithSubTasks>>
	fun completeTask(task: TaskItemModel): Completable
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
			Completable.fromCallable { taskDao.updateAllTaskLists(baseListResponse.items) }
		}
	}

	override fun getTask(parentId: String): Flowable<List<TaskWithSubTasks>> {
		return taskDao.getAll(parentId).flatMap { list ->
			Flowable.fromIterable(list).filter { task ->
				task.task.parent == null && task.task.deleted == null
			}.toList().toFlowable()
		}
	}

	override fun completeTask(
		task: TaskItemModel
	): Completable {
		return tasksApi.patchTask(
			task.parentId, task.id,
			Task(
				id = task.id,
				title = task.title,
				status = if (task.status != STATUS_COMPLETED) {
					STATUS_COMPLETED
				} else {
					STATUS_NEEDS_ACTION
				}

			)
		).doOnSuccess {
			fetchTasks(task.parentId).subscribeOn(Schedulers.io()).onErrorComplete().subscribe()
		}.ignoreElement()
	}
}