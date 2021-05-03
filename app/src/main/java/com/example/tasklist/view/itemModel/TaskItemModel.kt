package com.example.tasklist.view.itemModel

data class TaskItemModel(
	override val id: String,
	val title: String,
	val status: String,
	val taskListViewModel: (id: String) -> Unit
) : BaseItemModel() {
	fun click() {
		taskListViewModel(id)
	}
}