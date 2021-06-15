package com.example.tasklist.dev

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {
	val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

	@SuppressLint("SimpleDateFormat")
	@TypeConverter
	fun fromDate(value: Date): String {
		return SimpleDateFormat(dateFormat).format(value)
	}

	@SuppressLint("SimpleDateFormat")
	@TypeConverter
	fun toDate(value: String): Date? {
		return SimpleDateFormat(dateFormat).parse(value)
	}
}