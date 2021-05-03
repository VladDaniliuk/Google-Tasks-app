package com.example.tasklist.api.service

import com.example.tasklist.api.model.response.BaseListResponse
import com.example.tasklist.api.model.response.Task
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TasksApi {

	/*@DELETE("/tasks/v1/lists/{taskList}/tasks/{task}")
	fun deleteTask(
		@Path("taskList") taskListId: String, @Path("task") taskId: String
	)

	@GET("/tasks/v1/lists/{taskList}/tasks/{task}")
	fun getTask(
		@Path("taskList") taskListId: String, @Path("task") taskId: String
	): Call<Task>

	@POST("/tasks/v1/lists/{taskList}/tasks")
	fun insertTask(@Path("taskList") taskListId: String, @Body task: Task): Call<Task>
*/
	@GET("/tasks/v1/lists/{taskList}/tasks")
	fun getAllTasks(
		@Path("taskList") taskListId: String,
		@Query("showCompleted") showCompleted: Boolean = true,
		@Query("showHidden") showHidden: Boolean = true
	): Single<BaseListResponse<Task>>
/*
	@POST("/tasks/v1/lists/{taskList}/tasks/{task}/move")
	fun moveTask(
		@Path("taskList") taskListId: String,
		@Path("task") taskId: String
	): Call<Task>//request body must be empty

	@PATCH("/tasks/v1/lists/{taskList}/tasks/{task}")
	fun patchTask(
		@Path("taskList") taskListId: String, @Path("task") taskId: String,
		@Body task: Task
	): Call<Task>

	@PUT("/tasks/v1/lists/{taskList}/tasks/{task}")
	fun updateTask(
		@Path("taskList") taskListId: String, @Path("task") taskId: String,
		@Body task: Task
	): Call<Task>*/
}