package com.example.tasklist.db.dao

import androidx.room.*
import com.example.tasklist.api.model.response.TaskList
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
interface TaskListDao {
	@Delete
	fun delete(taskList: TaskList)

	@Query("SELECT * FROM taskList WHERE id IN(:id)")
	fun getTaskList(id: String): Single<TaskList>

	@Insert
	fun insertTaskList(taskList: TaskList): Completable

	@Query("SELECT * FROM taskList")
	fun getAll(): Observable<TaskList>

	@Update
	fun patchTaskList(taskList: TaskList): Single<TaskList>

	@Update
	fun updateTaskList(taskList: TaskList): Single<TaskList>
}