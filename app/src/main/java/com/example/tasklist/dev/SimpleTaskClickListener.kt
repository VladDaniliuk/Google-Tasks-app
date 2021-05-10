package com.example.tasklist.dev

import com.example.tasklist.view.itemModel.TaskItemModel

abstract class SimpleTaskClickListener: TaskItemModel.OnTaskClickListener {
	override fun onTaskItemClick(model: TaskItemModel) {}

	override fun onExpandItemClick(model: TaskItemModel) {}

	override fun onTaskExecuteClick(model: TaskItemModel) {}
}