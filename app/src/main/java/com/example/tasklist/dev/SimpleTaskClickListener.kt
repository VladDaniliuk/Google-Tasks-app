package com.example.tasklist.dev

import android.view.View
import com.example.tasklist.view.itemModel.TaskItemModel

abstract class SimpleTaskClickListener: TaskItemModel.OnTaskClickListener {
	override fun onTaskItemClick(model: TaskItemModel, view: View) {}

	override fun onExpandItemClick(model: TaskItemModel, view: View) {}

	override fun onTaskExecuteClick(model: TaskItemModel, view: View){}
}