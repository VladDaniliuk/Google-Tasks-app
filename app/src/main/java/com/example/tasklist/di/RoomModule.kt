package com.example.tasklist.di

import android.content.Context
import androidx.room.Room
import com.example.tasklist.db.dao.TaskListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

	@Provides
	@Singleton
	fun provideDatabase(@ApplicationContext applicationContext: Context): AppDatabase {
		return Room.databaseBuilder(
			applicationContext, AppDatabase::class.java, AppDatabase.DATABASE_NAME
		).build()
	}

	@Provides
	@Singleton
	fun provideTaskListDao(database: AppDatabase): TaskListDao {
		return database.taskListDao()
	}
}