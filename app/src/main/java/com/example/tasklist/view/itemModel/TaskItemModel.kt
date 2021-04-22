package com.example.tasklist.view.itemModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskItemModel(
	override val id: String,
	val title : String
): BaseItemModel()