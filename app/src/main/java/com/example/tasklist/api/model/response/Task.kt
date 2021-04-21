package com.example.tasklist.api.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
	val kind : String,
	@PrimaryKey val id : String,
	val etag : String,
	val title : String,
	val updated : String,
	val selfLink : String,
	val parent : String,
	val position : String,
	val notes : String,
	val status : String,
	val due : String,
	val completed : String,
	val deleted : Boolean,
	val hidden : Boolean,
	val links : List<Link>
)
data class Link (//?
	val type : String,
	val description : String,
	val link : String
)