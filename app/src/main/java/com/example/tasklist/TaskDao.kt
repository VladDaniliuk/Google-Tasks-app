package com.example.tasklist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query

@Dao
interface TaskDao {
    //?tasks.clear
    @Delete
    fun deleteTask(task: Task)

    /*@Query("SELECT")
    fun getTask(taskList: TaskList, task: Task): Task*/
}