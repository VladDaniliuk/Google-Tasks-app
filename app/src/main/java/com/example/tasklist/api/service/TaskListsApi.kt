package com.example.tasklist.api.service

import com.example.tasklist.api.model.response.BaseListResponse
import com.example.tasklist.api.model.response.TaskList
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface TaskListsApi {

	@DELETE("/tasks/v1/users/@me/lists/{taskListId}")
	fun deleteTaskList(@Path("taskListId") taskListId: String): Completable

	@POST("/tasks/v1/users/@me/lists")
	fun insertTaskList(@Body taskList: TaskList): Single<TaskList>

	@GET("/tasks/v1/users/@me/lists")
	fun getAllTaskLists(): Single<BaseListResponse<TaskList>>

	@PATCH("/tasks/v1/users/@me/lists/{taskList}")
	fun patchTaskList(
		@Path("taskList") taskListId: String,
		@Body taskList: TaskList
	): Single<TaskList>
}