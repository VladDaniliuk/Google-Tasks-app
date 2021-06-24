package com.example.tasklist.viewModel.taskListList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasklist.BR
import com.example.tasklist.R
import com.example.tasklist.databinding.LayoutTaskListBinding
import com.example.tasklist.domain.TaskListRepository
import com.example.tasklist.view.adapter.BaseItemAdapter
import com.example.tasklist.view.itemModel.TaskListItemModel
import com.example.tasklist.viewModel.baseViewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class TaskListListViewModel @Inject constructor(
	private val taskListRepository: TaskListRepository
) : BaseViewModel() {
	private val _list = MutableLiveData<List<TaskListItemModel>>()
	val list: LiveData<List<TaskListItemModel>> = _list

	val adapter = BaseItemAdapter<TaskListItemModel, LayoutTaskListBinding>(
		BR.model,
		R.layout.layout_task_list
	)

	init {
		taskListRepository.getTaskLists()
			.subscribeOn(Schedulers.computation())
			.map { m ->
				m.map { taskList ->
					TaskListItemModel(taskList.id, taskList.title) { id, view ->
						onBaseClick.postValue(id to view)
					}
				}
			}
			.subscribe {
				_list.postValue(it)
			}

		fetchBase()
	}

	override fun fetchBase(withAnim: Boolean?) {
		fetchInProgress.postValue(withAnim)
		taskListRepository.fetchTaskLists()
			.subscribeOn(Schedulers.io())
			.onErrorComplete()
			.doFinally {
				fetchInProgress.postValue(false)
			}
			.subscribe()
	}

	override fun deleteBase(id: String, forDelete: Boolean) {
		setTaskListClickable(id, false)
		taskListRepository.deleteTaskList(id).subscribeOn(Schedulers.io())
			.subscribe({
				onDeleteBaseResult.postValue(true)
			}, {
				setTaskListClickable(id, true)
				onDeleteBaseResult.postValue(false)
			})
	}

	/*
	* Filter all taskLists by id
	* So make it clickable/unclickable
	*/
	private fun setTaskListClickable(id: String, clickable: Boolean) {
		list.value?.find {
			it.id == id
		}?.clickable?.postValue(clickable)
	}
}
