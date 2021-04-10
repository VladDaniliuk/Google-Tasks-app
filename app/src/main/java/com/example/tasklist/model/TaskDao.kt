package com.example.tasklist.model

import androidx.room.*
import com.example.tasklist.Task

@Dao
interface TaskDao {
	//?tasks.clear

	@Delete
	fun deleteTask(task: Task<Any?>)

	@Query("SELECT * FROM task WHERE (id = :taskId)")//?
	fun getTask(taskList: TaskList, taskId: String): Task<Any?>

	@Insert
	fun insertTask(taskList: TaskList, taskParent: Task<Any?>, taskPrevious: Task<Any?>): Task<Any?>

	@Query("SELECT * FROM task")//?
	fun getAll(taskList: TaskList): List<Task<Any?>>

	@Query("UPDATE task SET position = 0 WHERE :parentTaskId = NULL")
	fun moveTask(taskListId: String, taskPosition: String, parentTaskId: String, previousTaskPosition: String): Task<Any?>

	@Update
	fun updateTask(task: Task<Any?>): Task<Any?>

	@Update
	fun patchTask(task: Task<Any?>): Task<Any?>
}

/*
* UPDATE task SET position = position - 1 WHERE (parent = :previousParentTaskId) AND (position > :taskPosition)
* UPDATE task SET position = position + 1 WHERE (parent = :parentTaskId) AND (position > :previousTaskPosition)
* UPDATE task SET position = :previousTaskPosition + 1 WHERE (id = :taskId)
*/