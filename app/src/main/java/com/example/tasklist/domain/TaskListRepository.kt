package com.example.tasklist.domain

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.tasklist.api.service.TaskListsApi
import com.example.tasklist.di.AppDatabase
import com.example.tasklist.di.RoomModule
import com.example.tasklist.view.itemModel.TaskListItemModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

interface TaskListRepository {
	fun onTaskListsUpload(context: Context): Boolean
	fun getTaskList(): Single<List<TaskListItemModel>>
	fun setTaskList(list: List<TaskListItemModel>, context: Context)
}

class TaskListRepositoryImpl @Inject constructor(
	private val taskListsApi: TaskListsApi
) : TaskListRepository {
	override fun onTaskListsUpload(context: Context): Boolean {
		val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
		return activeNetwork?.isConnectedOrConnecting == true
	}

	override fun getTaskList(): Single<List<TaskListItemModel>> {
		return taskListsApi.getAllTaskLists().map { m ->
			m.items.map { TaskListItemModel(it.id, it.title) }
		}
	}

	override fun setTaskList(list: List<TaskListItemModel>,context: Context) {
		val db = Room.databaseBuilder(
			context,
			AppDatabase::class.java, "task_list_database"
		).build()

		val taskListDao = db.taskListDao()
		taskListDao.insertAllTaskLists(list)
		//val taskListItemModel: List<TaskListItemModel> = taskListDao.getAll()
		Log.d("RER",taskListDao.getAll()[0].title)

		//db.taskListDao().insertAllTaskLists(list).subscribe({Log.d("SAVE", "true")},{Log.d("INTERNET", "false")})
	}
}