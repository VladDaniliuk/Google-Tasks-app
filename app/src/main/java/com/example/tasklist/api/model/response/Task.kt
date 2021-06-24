package com.example.tasklist.api.model.response

import androidx.annotation.StringDef
import androidx.room.*

@Entity
data class Task(
	@PrimaryKey val id: String,
	@ColumnInfo(name = "title") val title: String? = null,
	@ColumnInfo(name = "kind") val kind: String? = null,
	@ColumnInfo(name = "etag") val etag: String? = null,
	@ColumnInfo(name = "updated") val updated: String? = null,
	@ColumnInfo(name = "self_link") val selfLink: String? = null,
	@ColumnInfo(name = "parent") val parent: String? = null,
	@ColumnInfo(name = "position") val position: String? = null,
	@ColumnInfo(name = "notes") val notes: String? = null,
	@ColumnInfo(name = "status") @TaskStatus var status: String? = null,
	@ColumnInfo(name = "due") val due: String? = null,
	@ColumnInfo(name = "completed") val completed: String? = null,
	@ColumnInfo(name = "deleted") val deleted: Boolean? = null,
	@ColumnInfo(name = "hidden") val hidden: Boolean? = null,
	@ColumnInfo(name = "parent_id") var parentId: String? = null
)

data class TaskWithSubTasks(
	@Embedded val task: Task,
	@Relation(
		parentColumn = "id",
		entityColumn = "parent"
	)
	val subTasks: List<Task>
)

@Retention(AnnotationRetention.SOURCE)
@StringDef(STATUS_COMPLETED, STATUS_NEEDS_ACTION)
annotation class TaskStatus

const val STATUS_COMPLETED = "completed"
const val STATUS_NEEDS_ACTION = "needsAction"