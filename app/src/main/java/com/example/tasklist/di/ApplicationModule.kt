package com.example.tasklist.di

import com.example.tasklist.domain.*
import com.example.tasklist.model.PreferenceManager
import com.example.tasklist.model.PreferenceManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {

	@Binds
	abstract fun bindPreferenceManager(
		preferenceManagerImpl: PreferenceManagerImpl
	): PreferenceManager

	@Binds
	abstract fun bindLogInRepository(logInRepositoryImpl: LogInRepositoryImpl): LogInRepository

	@Binds
	abstract fun bindTaskListRepository(taskListRepositoryImpl: TaskListRepositoryImpl):
			TaskListRepository

	@Binds
	abstract fun bindTaskRepository(taskRepositoryImpl: TaskRepositoryImpl): TaskRepository
}