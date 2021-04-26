package com.example.tasklist.api.model.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskList (
	@ColumnInfo(name = "kind") val kind : String?,
	@PrimaryKey val id : String,
	@ColumnInfo(name = "etag") val etag : String?,
	@ColumnInfo(name = "title") val title : String,
	@ColumnInfo(name = "updated") val updated : String?,
	@ColumnInfo(name = "self_link") val selfLink : String?
)