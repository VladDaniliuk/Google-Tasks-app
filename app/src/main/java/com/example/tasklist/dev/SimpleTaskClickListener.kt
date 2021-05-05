package com.example.tasklist.dev

import com.example.tasklist.view.itemModel.TaskItemModel

abstract class SimpleTaskClickListener: TaskItemModel.OnTaskClickListener {
	override fun onTaskItemClick(id: String) {}

	override fun onExpandItemClick(id: String) {}

	override fun onTaskExecuteClick(id: String) {}
}