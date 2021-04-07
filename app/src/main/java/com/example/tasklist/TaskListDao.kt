package com.example.tasklist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

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

    @Query("UPDATE taskList SET title = :title WHERE id = :id")
    fun patchTaskList(id: String, title: String): TaskList

    @Query("UPDATE tasklist SET title = :title, id = :id WHERE id = :id")
    fun updateTaskList(id: String, title: String): TaskList
}