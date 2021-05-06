package com.example.tasklist.view.itemModel

import android.view.View
import com.example.tasklist.BR
import com.example.tasklist.R
import com.example.tasklist.databinding.LayoutTaskBinding
import com.example.tasklist.view.BaseItemAdapter

class TaskItemModel(
	override val id: String,
	val title: String,
	var status: String,
	val onTaskClickListener: OnTaskClickListener?,
	val list: List<TaskItemModel>? = null,
	var subTaskVisibility: Int = View.GONE
) : BaseItemModel() {

	val adapter = BaseItemAdapter<TaskItemModel, LayoutTaskBinding>(
		BR.model,
		R.layout.layout_sub_task
	).apply {
		submitList(list)
	}

	fun click() {
		onTaskClickListener?.onTaskItemClick(id)
	}

	fun onExpand() {
		onTaskClickListener?.onExpandItemClick(id)
	}

	fun onExecute() {
		onTaskClickListener?.onTaskExecuteClick(id)
	}

	override fun hashCode(): Int {
		var result = id.hashCode()
		result = 31 * result + title.hashCode()
		result = 31 * result + status.hashCode()
		result = 31 * result + subTaskVisibility
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
		if (subTaskVisibility != other.subTaskVisibility) return false
		if (list != other.list) return false

		return true
	}

	interface OnTaskClickListener {
		fun onTaskItemClick(id: String)
		fun onExpandItemClick(id: String)
		fun onTaskExecuteClick(id: String)
	}

	fun copy(
		id: String? = null,
		title: String? = null,
		status: String? = null,
		subTaskVisibility: Int? = null,
		onTaskClickListener: OnTaskClickListener? = null,
		list: List<TaskItemModel>? = null
	): TaskItemModel {
		return TaskItemModel(
			id ?: this.id,
			title ?: this.title,
			status ?: this.status,
			onTaskClickListener ?: this.onTaskClickListener,
			list ?: this.list,
			subTaskVisibility ?: this.subTaskVisibility
		)
	}
}