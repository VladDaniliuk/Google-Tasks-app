package com.example.tasklist.di

import com.example.tasklist.model.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {

	@Binds
	abstract fun bindPreferenceManager(preferenceManagerImpl: PreferenceManagerImpl): PreferenceManager

	@Binds
	abstract fun bindRetrofit(retrofitImpl: RetrofitImpl): RetrofitF

	@Binds
	abstract fun bindLogInRepository(logInRepositoryImpl: LogInRepositoryImpl): LogInRepository
}