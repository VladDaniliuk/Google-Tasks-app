package com.example.tasklist.viewModel

import androidx.lifecycle.ViewModel
import com.example.tasklist.domain.TaskListRepository
import javax.inject.Inject

class CreateTaskListViewModel @Inject constructor(
	private val taskListRepository: TaskListRepository
) : ViewModel() {

}