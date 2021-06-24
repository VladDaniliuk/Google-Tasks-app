package com.example.tasklist.extensions

import android.annotation.SuppressLint
import com.example.tasklist.api.model.response.TaskWithSubTasks
import java.text.SimpleDateFormat

class TaskComparator(private val setting: String?) : Comparator<TaskWithSubTasks> {
	@SuppressLint("SimpleDateFormat")
	override fun compare(o1: TaskWithSubTasks, o2: TaskWithSubTasks): Int {
		val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
		when {
			setting != "complete" -> {
				return 0
			}
			o1.task.due == null -> {
				return 1
			}
			o2.task.due == null -> {
				return -1
			}
			dateFormat.parse(o1.task.due)!! < dateFormat.parse(o2.task.due) -> {
				return -1
			}
			dateFormat.parse(o1.task.due)!! > dateFormat.parse(o2.task.due) -> {
				return 0
			}
			else -> return 1
		}
	}
}