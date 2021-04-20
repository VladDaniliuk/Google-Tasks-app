package com.example.tasklist.di

import com.example.tasklist.model.PreferenceManager
import com.example.tasklist.model.PreferenceManagerImpl
import com.example.tasklist.model.RetrofitF
import com.example.tasklist.model.RetrofitImpl
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
}