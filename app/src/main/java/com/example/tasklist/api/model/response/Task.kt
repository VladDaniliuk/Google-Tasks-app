package com.example.tasklist.api.model.response

import androidx.room.*

@Entity
data class Task(
	@ColumnInfo(name = "kind") val kind: String?,
	@PrimaryKey val id: String,
	@ColumnInfo(name = "etag") val etag: String?,
	@ColumnInfo(name = "title") val title: String,
	@ColumnInfo(name = "updated") val updated: String?,
	@ColumnInfo(name = "self_link") val selfLink: String?,
	@ColumnInfo(name = "parent") val parent: String?,
	@ColumnInfo(name = "position") val position: String?,
	@ColumnInfo(name = "notes") val notes: String?,
	@ColumnInfo(name = "status") val status: String,
	@ColumnInfo(name = "due") val due: String?,
	@ColumnInfo(name = "completed") val completed: String?,
	@ColumnInfo(name = "deleted") val deleted: Boolean?,
	@ColumnInfo(name = "hidden") val hidden: Boolean?,
	@ColumnInfo(name = "parent_id") var parentId: String?
)

data class TaskWithSubTasks(
	@Embedded val task: Task,
	@Relation(
		parentColumn = "id",
		entityColumn = "parent"
	)
	val subTasks: List<Task>
)