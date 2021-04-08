package com.example.tasklist

import androidx.room.*

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