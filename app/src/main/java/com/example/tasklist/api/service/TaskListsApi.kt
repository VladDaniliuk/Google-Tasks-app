package com.example.tasklist.api.service

import com.example.tasklist.api.model.response.BaseListResponse
import com.example.tasklist.api.model.response.BaseResponse
import com.example.tasklist.api.model.response.TaskList
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TaskListsApi {

	/*@DELETE("/tasks/v1/users/@me/lists/{taskListId}")
	fun deleteTaskList(@Path("taskListId") taskListId: String): Call<Void>

	@GET("/tasks/v1/users/@me/lists/{taskListId}")
	fun getTaskList(@Path("taskListId") taskListId: String): Call<TaskList>*/

	@POST("/tasks/v1/users/@me/lists")
	fun insertTaskList(@Body taskList: TaskList): Single<BaseResponse<TaskList>>

	@GET("/tasks/v1/users/@me/lists")
	fun getAllTaskLists(): Single<BaseListResponse<TaskList>>

	/*@PATCH("/tasks/v1/users/@me/lists/{taskList}")
	fun patchTaskList(@Path("taskList") taskListId: String, @Body taskList: TaskList):
			Call<TaskList>

	@PUT("/tasks/v1/users/@me/lists/{taskList}")
	fun updateTaskList(@Path("taskList") taskListId: String, @Body taskList: TaskList):
			Call<TaskList>

	@POST("/tasks/v1/lists/{taskList}/clear")
	fun clearTaskList(@Path("taskList") taskListId: String)*/
}