package com.example.tasklist.api.model.response

import androidx.annotation.NonNull
import androidx.room.*

@Entity
data class Task(
	@Embedded val taskWithoutLinks: TaskWithoutLinks,
	@Relation(parentColumn = "id", entity = Link::class, entityColumn = "id") val links: List<Link>
)

@Entity(
	foreignKeys = [
		ForeignKey(
			entity = TaskWithoutLinks::class, parentColumns = ["id"],
			childColumns = ["id"],
			onDelete = ForeignKey.CASCADE
		)],
	primaryKeys = ["id"]
)
data class Link(
	@NonNull
	@ColumnInfo(name = "id") val id: String,
	@ColumnInfo(name = "type") val type: String,
	@ColumnInfo(name = "description") val description: String,
	@ColumnInfo(name = "link") val link: String
)

@Entity
data class TaskWithoutLinks(
	@ColumnInfo(name = "kind") val kind: String,
	@NonNull
	@PrimaryKey val id: String,
	@ColumnInfo(name = "etag") val etag: String,
	@ColumnInfo(name = "title") val title: String,
	@ColumnInfo(name = "updated") val updated: String,
	@ColumnInfo(name = "self_link") val selfLink: String,
	@ColumnInfo(name = "parent") val parent: String,
	@ColumnInfo(name = "position") val position: String,
	@ColumnInfo(name = "notes") val notes: String,
	@ColumnInfo(name = "status") val status: String,
	@ColumnInfo(name = "due") val due: String,
	@ColumnInfo(name = "completed") val completed: String,
	@ColumnInfo(name = "deleted") val deleted: Boolean,
	@ColumnInfo(name = "hidden") val hidden: Boolean
)