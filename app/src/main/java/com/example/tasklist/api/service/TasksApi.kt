package com.example.tasklist.api.service

import com.example.tasklist.api.model.response.Task
import retrofit2.Call
import retrofit2.http.*

interface 	TasksApi {

	@DELETE("/tasks/v1/lists/{taskList}/tasks/{task}")
	fun deleteTask(
		@Path("taskList") taskListId: String, @Path("task") taskId: String
	)

	@GET("/tasks/v1/lists/{taskList}/tasks/{task}")
	fun getTask(
		@Path("taskList") taskListId: String, @Path("task") taskId: String
	): Call<Task>

	@POST("/tasks/v1/lists/{taskList}/tasks")
	fun insertTask(@Path("taskList") taskListId: String, @Body task: Task): Call<Task>

	@GET("/tasks/v1/lists/{taskList}/tasks")
	fun getAllTasks(@Path("taskList") taskListId: String): Call<List<Task>>

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
	): Call<Task>
}