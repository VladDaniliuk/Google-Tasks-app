package com.example.tasklist

import androidx.room.*

@Dao
interface TaskDao {
    //?tasks.clear

    @Delete
    fun deleteTask(task: Task)

    @Query("SELECT * FROM task WHERE (id = :taskId)")//?
    fun getTask(taskList: TaskList, taskId: String): Task

    @Insert
    fun insertTask(taskList: TaskList, taskParent: Task, taskPrevious: Task): Task

    @Query("SELECT * FROM task")//?
    fun getAll(taskList: TaskList): List<Task>

    @Query("UPDATE task SET position = 0 WHERE :parentTaskId = NULL")
    fun moveTask(taskListId: String, taskPosition: String, parentTaskId: String, previousTaskPosition: String): Task

    @Update
    fun updateTask(task: Task): Task

    @Update
    fun patchTask(task: Task): Task
}

/*
* UPDATE task SET position = position - 1 WHERE (parent = :previousParentTaskId) AND (position > :taskPosition)
* UPDATE task SET position = position + 1 WHERE (parent = :parentTaskId) AND (position > :previousTaskPosition)
* UPDATE task SET position = :previousTaskPosition + 1 WHERE (id = :taskId)
*/