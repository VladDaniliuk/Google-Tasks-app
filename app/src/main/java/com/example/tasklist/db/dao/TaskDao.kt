package com.example.tasklist.db.dao

import androidx.room.*
import com.example.tasklist.api.model.response.Task
import com.example.tasklist.api.model.response.TaskList
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface TaskDao {
	//?tasks.clear

	/*@Delete
	fun deleteTask(task: Task): Completable

	@Query("SELECT * FROM task WHERE (id = :taskId)")
	fun getTask(taskList: TaskList, taskId: String): Single<Task>

	@Insert
	fun insertTask(taskList: TaskList, taskParent: Task, taskPrevious: Task): Completable

	@Query("SELECT * FROM task")
	fun getAll(taskList: TaskList): Single<List<Task>>

	@Query("UPDATE task SET position = 0 WHERE :parentTaskId = NULL")
	fun moveTask(taskListId: String, taskPosition: String, parentTaskId: String, previousTaskPosition: String): Single<Task>

	@Update
	fun updateTask(task: Task): Completable

	@Update
	fun patchTask(task: Task): Completable*/
}