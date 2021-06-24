package com.example.tasklist.api.service

import com.example.tasklist.api.model.response.BaseListResponse
import com.example.tasklist.api.model.response.Task
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface TasksApi {

	/*@DELETE("/tasks/v1/lists/{taskList}/tasks/{task}")
	fun deleteTask(
		@Path("taskList") taskListId: String, @Path("task") taskId: String
	)*/

	@GET("/tasks/v1/lists/{taskList}/tasks/{task}")
	fun getTask(@Path("taskList") taskListId: String, @Path("task") taskId: String): Single<Task>

	@POST("/tasks/v1/lists/{taskList}/tasks")
	fun insertTask(
		@Path("taskList") taskListId: String,
		@Query("parent") parentId: String?,
		@Body task: Task
	): Single<Task>

	@GET("/tasks/v1/lists/{taskList}/tasks")
	fun getAllTasks(
		@Path("taskList") taskListId: String,
		@Query("showCompleted") showCompleted: Boolean = true,
		@Query("showDeleted") showDeleted: Boolean = true,
		@Query("showHidden") showHidden: Boolean = true,
		@Query("maxResults") maxResults: Int = 100
	): Single<BaseListResponse<Task>>


	@POST("/tasks/v1/lists/{taskList}/tasks/{task}/move")
	fun moveTask(
		@Path("taskList") taskListId: String,
		@Path("task") taskId: String,
		@Query("previous") previousTaskId: String?
	): Single<Task>

	@PATCH("/tasks/v1/lists/{taskList}/tasks/{task}")
	fun patchTask(
		@Path("taskList") taskListId: String, @Path("task") taskId: String,
		@Body task: Task
	): Single<Task>
/*
	@PUT("/tasks/v1/lists/{taskList}/tasks/{task}")
	fun updateTask(
		@Path("taskList") taskListId: String, @Path("task") taskId: String,
		@Body task: Task
	): Call<Task>*/
}