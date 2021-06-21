package com.example.tasklist.db.dao

import androidx.room.*
import com.example.tasklist.api.model.response.Task
import com.example.tasklist.api.model.response.TaskWithSubTasks
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface TaskDao {
	//?tasks.clear

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertAllTasks(list: List<Task>): Completable

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertTask(task: Task): Completable

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertTaskSync(task: Task)

	@Query("SELECT * FROM Task WHERE (parent_id = :parentId)")
	fun getAll(parentId: String): Flowable<List<TaskWithSubTasks>>

	@Query("DELETE FROM Task")
	fun deleteAllSync()

	@Query("DELETE FROM Task WHERE (parent_id = :parentId AND id = :taskId)")
	fun deleteTaskSync(parentId: String, taskId: String)

	@Insert
	fun insertAllTasksSync(list: List<Task>)

	@Query("SELECT * FROM Task WHERE (parent_id = :parentId AND id = :taskId)")
	fun getTask(parentId: String, taskId: String): Flowable<TaskWithSubTasks>

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

	@Transaction
	fun updateTask(task: Task) {
		deleteTaskSync(task.parentId!!, task.id)
		insertTaskSync(task)
	}

	@Transaction
	fun updateAllTasks(list: List<Task>) {
		deleteAllSync()
		insertAllTasksSync(list)
	}
}