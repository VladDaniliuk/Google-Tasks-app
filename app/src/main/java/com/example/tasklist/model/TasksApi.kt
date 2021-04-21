package com.example.tasklist.model

import com.example.tasklist.Task
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.*

interface TasksApi {

	@DELETE("/tasks/v1/users/@me/lists/{taskListId}")
	fun deleteTaskList(@Path("taskListId") taskListId: String): Call<Void>

	@GET("/tasks/v1/users/@me/lists/{taskListId}")
	fun getTaskList(@Path("taskListId") taskListId: String): Call<TaskList>

	@POST("/tasks/v1/users/@me/lists")
	fun insertTaskList(@Body taskList: TaskList): Call<TaskList>

	@GET("/tasks/v1/users/@me/lists")
	fun getALLTaskLists(@Header("Authorization") token: String): Single<List<TaskList>>

	@PATCH("/tasks/v1/users/@me/lists/{taskList}")
	fun patchTaskList(@Path("taskList") taskListId: String, @Body taskList: TaskList):
			Call<TaskList>

	@PUT("/tasks/v1/users/@me/lists/{taskList}")
	fun updateTaskList(@Path("taskList") taskListId: String, @Body taskList: TaskList):
			Call<TaskList>

	@POST("/tasks/v1/lists/{taskList}/clear")
	fun clearTaskList(@Path("taskList") taskListId: String)

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

	@POST("https://oauth2.googleapis.com/token")
	fun getToken(@Body body: GoogleAuthTokenBody): Single<AccessTokenBody>
}