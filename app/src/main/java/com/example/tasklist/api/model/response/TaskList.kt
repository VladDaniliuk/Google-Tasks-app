package com.example.tasklist.api.model.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TaskList")
data class TaskList(
	@PrimaryKey val id: String,
	@ColumnInfo(name = "title") val title: String,
	@ColumnInfo(name = "kind") val kind: String? = null,
	@ColumnInfo(name = "etag") val etag: String? = null,
	@ColumnInfo(name = "updated") val updated: String? = null,
	@ColumnInfo(name = "self_link") val selfLink: String? = null
)