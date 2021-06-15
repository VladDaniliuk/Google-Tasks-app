package com.example.tasklist.di

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tasklist.api.model.response.Task
import com.example.tasklist.api.model.response.TaskList
import com.example.tasklist.db.dao.TaskDao
import com.example.tasklist.db.dao.TaskListDao
import com.example.tasklist.dev.DateConverter

@Database(entities = [Task::class, TaskList::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
	abstract fun taskListDao(): TaskListDao

	abstract fun taskDao(): TaskDao

	companion object {
		const val DATABASE_NAME = "task_list_database"
	}
}