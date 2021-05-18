package com.example.tasklist.view.itemModel

data class TaskListItemModel(
	override val id: String,
	val title: String,
	var clickable: Boolean = true,
	val taskListListViewModel: (id: String) -> Unit
) : BaseItemModel() {
	fun click() {
		taskListListViewModel(id)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as TaskListItemModel

		if (id != other.id) return false
		if (title != other.title) return false

		return true
	}

	override fun hashCode(): Int {
		var result = id.hashCode()
		result = 31 * result + title.hashCode()
		result = 31 * result + taskListListViewModel.hashCode()
		return result
	}
}