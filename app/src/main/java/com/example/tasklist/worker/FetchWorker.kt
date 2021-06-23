package com.example.tasklist.worker

import android.content.Context
import android.util.Log
import androidx.work.WorkerParameters
import androidx.work.rxjava3.RxWorker
import com.example.tasklist.domain.TaskListRepository
import com.example.tasklist.domain.TaskRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class FetchWorker @Inject constructor(
	context: Context,
	workerParams: WorkerParameters,
	private val taskListRepository: TaskListRepository,
	private val taskRepository: TaskRepository
) : RxWorker(context, workerParams) {
	override fun createWork(): Single<Result> {
		Log.e("Create Work", "TEST")
		return taskListRepository.fetchTaskLists().subscribeOn(Schedulers.io()).toSingleDefault(Result.success())
			/*.andThen(
				taskListRepository.getTaskLists()
					.flatMapCompletable { tasks ->
						Timber.e("${tasks.size}")
						Completable.merge(tasks.map {
							taskRepository.fetchTasks(it.id)
								.subscribeOn(Schedulers.io())
						})
					}.subscribeOn(Schedulers.io())
			).subscribeOn(Schedulers.io())
			.toSingleDefault(Result.success())
			.onErrorReturnItem(Result.failure())*/
	}
	/*override fun startWork(): ListenableFuture<Result> {
		return  taskListRepository.getTaskList().map {
			Result.success()
		}
		taskListRepository.fetchTaskLists()
		taskListRepository.fetchTaskLists()
		/*taskListRepository.getTaskLists().subscribe { list ->
			list.forEach {*/
		taskRepository.fetchTasks("OHh4dVdYZkFUa3E3YndMTQ")
		/*}
	}*/

		/*.subscribeOn(Schedulers.computation())
		.andThen {
			taskListRepository.getTaskLists()
				.subscribeOn(Schedulers.computation())
				.subscribe({
					Timber.v(it[0].title)
				}, {
					Timber.v("4")
				})
		}*/
		/*.subscribe({
			Timber.v("1")
		}, {
			Timber.v("2")
		})*/
		Timber.v("3")
		return SettableFuture.create()
	}*/
}