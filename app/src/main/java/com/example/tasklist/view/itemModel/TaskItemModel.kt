package com.example.tasklist.view.itemModel

import android.view.View
import com.example.tasklist.BR
import com.example.tasklist.R
import com.example.tasklist.databinding.LayoutTaskBinding
import com.example.tasklist.view.BaseItemAdapter

class TaskItemModel(
	override val id: String,
	val parentId: String,
	val title: String,
	var status: String,
	val dueDate: String? = null,
	private val onTaskClickListener: OnTaskClickListener?,
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
		onTaskClickListener?.onTaskItemClick(this)
	}

	fun onExpand() {
		onTaskClickListener?.onExpandItemClick(this)
	}

	fun onExecute() {
		onTaskClickListener?.onTaskExecuteClick(this)
	}

	override fun hashCode(): Int {
		var result = id.hashCode()
		result = 31 * result + parentId.hashCode()
		result = 31 * result + title.hashCode()
		result = 31 * result + status.hashCode()
		result = 31 * result + dueDate?.hashCode()!!
		result = 31 * result + subTaskVisibility
		result = 31 * result + (list?.hashCode() ?: 0)
		return result
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as TaskItemModel

		if (id != other.id) return false
		if (parentId != other.parentId) return false
		if (title != other.title) return false
		if (status != other.status) return false
		if (dueDate != other.dueDate) return false
		if (subTaskVisibility != other.subTaskVisibility) return false
		if (list != other.list) return false

		return true
	}

	interface OnTaskClickListener {
		fun onTaskItemClick(model: TaskItemModel)
		fun onExpandItemClick(model: TaskItemModel)
		fun onTaskExecuteClick(model: TaskItemModel)
	}

	fun copy(
		id: String? = null,
		parentId: String? = null,
		title: String? = null,
		status: String? = null,
		onTaskClickListener: OnTaskClickListener? = null,
		dueDate: String? = null,
		list: List<TaskItemModel>? = null,
		subTaskVisibility: Int? = null
	): TaskItemModel {
		return TaskItemModel(
			id ?: this.id,
			parentId ?: this.parentId,
			title ?: this.title,
			status ?: this.status,
			dueDate ?: this.dueDate,
			onTaskClickListener ?: this.onTaskClickListener,
			list ?: this.list,
			subTaskVisibility ?: this.subTaskVisibility
		)
	}
}