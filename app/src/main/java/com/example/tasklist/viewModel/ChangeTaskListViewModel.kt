package com.example.tasklist.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeTaskListViewModel @Inject constructor(
	/*private val taskListRepository: TaskListRepository*/
) : ViewModel() {

	val isClicked = MutableLiveData(false)
	val taskName = MutableLiveData("")

	/*private val onChangeTaskListClick = SingleLiveEvent<Unit>()
	val onChangeTaskListFinish = SingleLiveEvent<Unit>()*/

	/*val changeTaskListClickListener = View.OnClickListener {
		onChangeTaskListClick.call()
	}*/

	/*fun changeTaskClick() {
		isClicked.postValue(true)
		taskListRepository
	}*/
}