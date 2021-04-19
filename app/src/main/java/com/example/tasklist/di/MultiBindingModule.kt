package com.example.tasklist.di

import android.app.Application
import android.content.Context
import com.example.tasklist.model.PreferenceManager
import com.example.tasklist.model.PreferenceManagerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MultiBindingModule {

}