package com.example.tasklist.db.dao

import androidx.room.*
import com.example.tasklist.api.model.response.TaskList

@Dao
interface TaskListDao {
	@Delete
	fun delete(taskList: TaskList)

	@Query("SELECT * FROM taskList WHERE id IN(:id)")//?taskList
	fun getTaskList(id: String): TaskList

	@Insert
	fun insertTaskList(taskList: TaskList)

	@Query("SELECT * FROM taskList")
	fun getAll(): List<TaskList>

	@Update
	fun patchTaskList(taskList: TaskList): TaskList

	@Update
	fun updateTaskList(taskList: TaskList): TaskList
}