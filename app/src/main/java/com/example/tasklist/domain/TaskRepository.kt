package com.example.tasklist.domain

import com.example.tasklist.api.model.response.STATUS_COMPLETED
import com.example.tasklist.api.model.response.STATUS_NEEDS_ACTION
import com.example.tasklist.api.model.response.Task
import com.example.tasklist.api.model.response.TaskWithSubTasks
import com.example.tasklist.api.service.TasksApi
import com.example.tasklist.db.dao.TaskDao
import com.example.tasklist.extensions.TaskComparator
import com.example.tasklist.view.itemModel.TaskItemModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

interface TaskRepository {
	fun changeTask(taskListId: String, task: Task): Completable
	fun completeTask(task: TaskItemModel): Completable
	fun createTask(
		taskListId: String,
		parentId: String?,
		title: String,
		dueDate: String?
	): Completable

	fun fetchTask(parentId: String, taskId: String): Completable
	fun fetchTasks(taskListId: String): Completable
	fun getTask(parentId: String, taskId: String): Flowable<TaskWithSubTasks>
	fun getTasks(
		parentId: String,
		setting: Triple<String, String, String>? = null
	): Flowable<List<TaskWithSubTasks>>

	fun onDeleteTask(taskListId: String, taskId: String, onDelete: Boolean): Completable
}

class TaskRepositoryImpl @Inject constructor(
	private val tasksApi: TasksApi,
	private val taskDao: TaskDao
) : TaskRepository {
	override fun changeTask(taskListId: String, task: Task): Completable {
		return tasksApi.patchTask(taskListId, task.id, task).flatMapCompletable { taskItem ->
			taskItem.parentId = taskListId
			Completable.fromCallable { taskDao.updateTask(taskItem) }
		}
	}

	override fun fetchTasks(taskListId: String): Completable {
		return tasksApi.getAllTasks(taskListId).flatMapCompletable { baseListResponse ->
			baseListResponse.items.forEach {
				it.parentId = taskListId
			}
			Completable.fromCallable { taskDao.updateAllTaskLists(baseListResponse.items) }
		}
	}

	override fun fetchTask(parentId: String, taskId: String): Completable {
		return tasksApi.getTask(parentId, taskId).flatMapCompletable { taskItem ->
			taskItem.parentId = parentId
			Completable.fromCallable { taskDao.updateTask(taskItem) }
		}
	}

	override fun getTask(parentId: String, taskId: String): Flowable<TaskWithSubTasks> {
		return taskDao.getTask(parentId, taskId)
	}

	override fun getTasks(
		parentId: String,
		setting: Triple<String, String, String>?
	): Flowable<List<TaskWithSubTasks>> {
		return taskDao.getAll(parentId).flatMap { list ->
			Flowable.fromIterable(list).filter { task ->
				(task.task.parent == null) and
						when (setting?.first) {
							"Hide" -> task.task.completed == null
							else -> true
						} and
						when (setting?.third) {
							"deleted" -> task.task.deleted != null
							else -> task.task.deleted == null
						}
			}.toSortedList(TaskComparator(setting?.second))
				.toFlowable()
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
		}.flatMapCompletable {
			Completable.complete()
		}
	}

	override fun createTask(
		taskListId: String,
		parentId: String?,
		title: String,
		dueDate: String?
	): Completable {
		return tasksApi.insertTask(taskListId, parentId, Task("", title, due = dueDate))
			.flatMapCompletable {
				fetchTasks(taskListId)
			}
	}

	override fun onDeleteTask(
		taskListId: String,
		taskId: String,
		onDelete: Boolean
	): Completable {
		return tasksApi.patchTask(taskListId, taskId, Task(id = taskId, deleted = onDelete))
			.flatMapCompletable {
				fetchTasks(taskListId)
			}
	}
}