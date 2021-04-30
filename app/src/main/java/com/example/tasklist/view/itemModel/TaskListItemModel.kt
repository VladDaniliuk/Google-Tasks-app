package com.example.tasklist.view.itemModel

data class TaskListItemModel(
	override val id: String,
	val title: String,
	val taskListListViewModel: (id: String) -> Unit,
) : BaseItemModel() {
	fun click() {
		taskListListViewModel(id)
	}
}