package com.example.tasklist.extensions

import android.annotation.SuppressLint
import com.example.tasklist.R
import com.example.tasklist.api.model.response.TaskWithSubTasks
import java.math.BigInteger
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
				else -> dateFormat.parse(o1.task.due)!!.compareTo(dateFormat.parse(o2.task.due))
			}
			else -> (BigInteger(o1.task.position ?: "0")).compareTo(BigInteger(o2.task.position ?: "0"))
		}
	}
}
