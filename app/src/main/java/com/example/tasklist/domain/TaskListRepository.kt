package com.example.tasklist.domain

import com.example.tasklist.api.service.TaskListsApi
import com.example.tasklist.model.PreferenceManager
import javax.inject.Inject

interface TaskListRepository {
	fun onTaskListsUpload()
}

class TaskListRepositoryImpl @Inject constructor(
	private val taskListsApi: TaskListsApi,
	private val preferenceManager: PreferenceManager
) : TaskListRepository {
	override fun onTaskListsUpload() {
		//return taskListsApi.getALLTaskLists(preferenceManager.getToken!!)
	}
}