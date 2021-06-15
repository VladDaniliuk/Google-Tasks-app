package com.example.tasklist.view.itemModel

import android.view.View
import com.example.tasklist.BR
import com.example.tasklist.R
import com.example.tasklist.databinding.LayoutTaskBinding
import com.example.tasklist.view.adapter.BaseItemAdapter
import java.util.*

class TaskItemModel(
	override val id: String,
	val parentId: String,
	override val title: String,
	var status: String,
	val dueDate: Date? = null,
	var notes: String? = null,
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

	fun click(view: View) {
		onTaskClickListener?.onTaskItemClick(this, view)
	}

	fun onExpand(view: View) {
		onTaskClickListener?.onExpandItemClick(this, view)
	}

	fun onExecute(view: View) {
		onTaskClickListener?.onTaskExecuteClick(this, view)
	}

	override fun hashCode(): Int {
		var result = id.hashCode()
		result = 31 * result + parentId.hashCode()
		result = 31 * result + title.hashCode()
		result = 31 * result + status.hashCode()
		result = 31 * result + dueDate?.hashCode()!!
		result = 31 * result + subTaskVisibility
		result = 31 * result + (list?.hashCode() ?: 0)
		result = 31 * result + notes?.hashCode()!!
		//result = 31 * result + clickable.hashCode()
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
		if (notes != other.notes) return false
		//if (clickable != other.clickable) return false

		return true
	}

	interface OnTaskClickListener {
		fun onTaskItemClick(model: TaskItemModel, view: View)
		fun onExpandItemClick(model: TaskItemModel, view: View)
		fun onTaskExecuteClick(model: TaskItemModel, view: View)
	}

	fun copy(
		id: String? = null,
		parentId: String? = null,
		title: String? = null,
		status: String? = null,
		onTaskClickListener: OnTaskClickListener? = null,
		dueDate: Date? = null,
		notes: String? = null,
		list: List<TaskItemModel>? = null,
		subTaskVisibility: Int? = null
	): TaskItemModel {
		return TaskItemModel(
			id ?: this.id,
			parentId ?: this.parentId,
			title ?: this.title,
			status ?: this.status,
			dueDate ?: this.dueDate,
			notes ?: this.notes,
			onTaskClickListener ?: this.onTaskClickListener,
			list ?: this.list,
			subTaskVisibility ?: this.subTaskVisibility
		)
	}
}