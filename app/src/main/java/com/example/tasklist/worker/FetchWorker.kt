package com.example.tasklist.worker

import android.content.Context
import androidx.work.WorkerParameters
import androidx.work.rxjava3.RxWorker
import com.example.tasklist.domain.TaskListRepository
import com.example.tasklist.domain.TaskRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltWorker
class FetchWorker @AssistedInject constructor(
	@Assisted context: Context,
	@Assested workerParams: WorkerParameters,
	private val taskListRepository: TaskListRepository,
	private val taskRepository: TaskRepository
) : RxWorker(context, workerParams) {
	override fun createWork(): Single<Result> {
		return taskListRepository.fetchTaskLists().andThen(
			taskListRepository.getTaskLists().firstOrError()
		).flatMapCompletable { tasks ->
			Completable.merge(tasks.map {
				Timber.tag("Task").e(it.id)
				taskRepository.fetchTasks(it.id).subscribeOn(Schedulers.io())
			})
		}.subscribeOn(Schedulers.io()).toSingleDefault(Result.success())
			.onErrorReturnItem(Result.failure())
	}
}