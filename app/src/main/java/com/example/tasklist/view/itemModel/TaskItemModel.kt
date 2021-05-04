package com.example.tasklist.view.itemModel

import com.example.tasklist.BR
import com.example.tasklist.R
import com.example.tasklist.databinding.LayoutTaskBinding
import com.example.tasklist.view.BaseItemAdapter

class TaskItemModel(
	override val id: String,
	val title: String,
	val status: String,
	val onTaskItemClick: (id: String) -> Unit,
	val onExpandItemClick: (id: String) -> Unit,
	val onTaskExecuteClick: (id: String) -> Unit,
	val list: List<TaskItemModel>?
) : BaseItemModel() {

	val adapter = BaseItemAdapter<TaskItemModel, LayoutTaskBinding>(
		BR.model,
		R.layout.layout_sub_task
	).apply {
		submitList(list)
	}

	fun click() {
		onTaskItemClick(id)
	}

	fun onExpand() {
		onExpandItemClick(id)
	}

	fun onExecute() {
		onTaskExecuteClick(id)
	}

	override fun hashCode(): Int {
		var result = id.hashCode()
		result = 31 * result + title.hashCode()
		result = 31 * result + status.hashCode()
		result = 31 * result + (list?.hashCode() ?: 0)
		return result
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as TaskItemModel

		if (id != other.id) return false
		if (title != other.title) return false
		if (status != other.status) return false
		if (list != other.list) return false

		return true
	}
}