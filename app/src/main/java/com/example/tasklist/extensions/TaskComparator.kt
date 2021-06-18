package com.example.tasklist.extensions

import android.annotation.SuppressLint
import com.example.tasklist.R
import com.example.tasklist.api.model.response.TaskWithSubTasks
import java.text.SimpleDateFormat

class TaskComparator(private val setting: Int?) : Comparator<TaskWithSubTasks> {
	@SuppressLint("SimpleDateFormat")
	override fun compare(o1: TaskWithSubTasks, o2: TaskWithSubTasks): Int {
		val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
		return when (setting) {
			R.id.date_to_add -> 0
			R.id.date_to_complete -> when {
				o1.task.due == null -> 1
				o2.task.due == null -> -1
				dateFormat.parse(o1.task.due)!! < dateFormat.parse(o2.task.due) -> -1
				dateFormat.parse(o1.task.due)!! > dateFormat.parse(o2.task.due) -> 0
				else -> 1
			}
			else -> when {
				o1.task.position?.toInt() ?: 0 < o2.task.position?.toInt() ?: 0 -> -1
				o1.task.position?.toInt() ?: 0 > o2.task.position?.toInt() ?: 0 -> 0
				else -> 1
			}
		}
	}
}