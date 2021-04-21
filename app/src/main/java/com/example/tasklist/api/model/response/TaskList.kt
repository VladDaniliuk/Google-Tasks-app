package com.example.tasklist.api.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskList (
	val kind : String?,
	@PrimaryKey val id : String?,
	val etag : String?,
	val title : String?,
	val updated : String?,
	val selfLink : String?
)