package com.example.tasklist.view.itemModel

import android.view.View

data class TaskListItemModel(
	override val id: String,
	override val title: String,
	val taskListListViewModel: ((id: String, view: View) -> Unit)? = null
) : BaseItemModel() {
	fun click(view: View) {
		taskListListViewModel?.invoke(id, view)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as TaskListItemModel

		if (id != other.id) return false
		if (title != other.title) return false
		//if (clickable != other.clickable) return false

		return true
	}

	override fun hashCode(): Int {
		var result = id.hashCode()
		result = 31 * result + title.hashCode()
		result = 31 * result + clickable.hashCode()
		result = 31 * result + taskListListViewModel.hashCode()
		return result
	}

	override fun toString(): String {
		return this.title
	}
}