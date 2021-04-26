package com.example.tasklist.viewModel

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.tasklist.db.dao.TaskListDao
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.di.AppDatabase
import com.example.tasklist.domain.TaskListRepository
import com.example.tasklist.view.itemModel.TaskListItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TaskListListViewModel @Inject constructor(
	private val taskListRepository: TaskListRepository,
	private val taskListDao: TaskListDao
) : ViewModel() {

	var list = MutableLiveData<List<TaskListItemModel>>()

	val onCreateTaskListClick = SingleLiveEvent<Unit>()
	val onGetTaskList = SingleLiveEvent<Unit>()

	val createTaskListClickListener = View.OnClickListener {
		onCreateTaskListClick.call()
	}

	fun insertTaskLists(list: MutableLiveData<List<TaskListItemModel>>) {
		taskListDao.insertAllTaskLists(list.value!!)
		Log.d("GOOD", "true")
	}

	fun checkInternet(context: Context) {
		if (taskListRepository.onTaskListsUpload(context)) {
			Log.d("INTERNET", "true")

			taskListRepository.getTaskList()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe({
					list.postValue(it)
					onGetTaskList.call()
					/*val db = Room.databaseBuilder(
						context,
						AppDatabase::class.java,
						"task_list_database"
					).build()*/

					//taskListDao.insertAllTaskLists(it)

					/*val taskListDao = db.taskListDao()
					//taskListDao.insertAllTaskLists(list)
					val taskListItemModel: List<TaskListItemModel> = taskListDao.getAll()
					Log.d("RER",taskListItemModel.get(0).title)*/

					//taskListRepository.setTaskList(it, context)

					//db.taskListDao().insertAllTaskLists(list.value).doOnComplete{Log.d("SAVE", "true")}
					//db.taskListDao().insertAllTaskLists(list.value).subscribe({Log.d("SAVE", "true")},{Log.d("SAVE", "false")})
				}, { Log.d("GET", "false") })
		} else {
			Log.d("INTERNET", "false")
		}
	}
}
