package com.example.tasklist.db.dao

import androidx.room.*
import com.example.tasklist.api.model.response.TaskList
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface TaskListDao {
	@Delete
	fun delete(taskList: TaskList): Completable

	@Query("DELETE FROM TaskList")
	fun deleteAllSync()

	@Query("SELECT * FROM TaskList WHERE id IN(:id)")
	fun getTaskList(id: String): Single<TaskList>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertAllTaskLists(list: List<TaskList>): Completable

	@Insert
	fun insertAllTaskListsSync(list: List<TaskList>)

	@Insert
	fun insertTaskList(taskList: TaskList): Completable

	@Query("SELECT * FROM TaskList")
	fun getAll(): Flowable<List<TaskList>>

	@Update
	fun patchTaskList(taskList: TaskList): Completable

	@Update
	fun updateTaskList(taskList: TaskList): Completable

	@Transaction
	fun updateAllTaskLists(list: List<TaskList>) {
		deleteAllSync()
		insertAllTaskListsSync(list)
	}
}