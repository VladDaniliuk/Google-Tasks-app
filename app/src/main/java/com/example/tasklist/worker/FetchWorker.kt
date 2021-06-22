package com.example.tasklist.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.tasklist.domain.TaskListRepository
import com.example.tasklist.domain.TaskRepository
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class FetchWorker @Inject constructor(
	context: Context,
	workerParams: WorkerParameters,
	private val taskListRepository: TaskListRepository,
	private val taskRepository: TaskRepository
) : Worker(context, workerParams) {
	override fun doWork(): Result {
		taskListRepository.fetchTaskLists()
			.subscribeOn(Schedulers.computation()).andThen(
				taskListRepository.getTaskLists()
					.subscribeOn(Schedulers.computation())
			).subscribe({ Timber.v("Good ListList") }, { Timber.v("Bad ListList") })
		taskListRepository.getTaskLists()
			.subscribeOn(Schedulers.computation())
			.subscribe({ list ->
				list.forEach { taskList ->
					taskRepository.fetchTasks(taskList.id)
						.subscribeOn(Schedulers.computation()).subscribe({
						}, {})
				}
			}, {})

		Timber.v("Here")
		return Result.success()
	}
}