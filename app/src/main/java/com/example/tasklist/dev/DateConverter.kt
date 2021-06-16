package com.example.tasklist.dev

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import com.google.api.client.util.DateTime
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {
	private val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

	@SuppressLint("SimpleDateFormat")
	@TypeConverter
	fun fromDate(value: DateTime?): String? {
		return when (value) {
			null -> null
			else -> value.toString()
		}
	}

	@SuppressLint("SimpleDateFormat")
	@TypeConverter
	fun toDate(value: String?): DateTime? {
		return when (value) {
			null -> null
			else -> DateTime.parseRfc3339(value)
		}
	}
}