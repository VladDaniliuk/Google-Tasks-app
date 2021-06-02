package com.example.tasklist.extensions

import android.text.Editable
import android.text.TextWatcher
import com.example.tasklist.dev.SingleLiveEvent
import java.util.*

class DebounceTextWatcher(private val onItem: SingleLiveEvent<String>) : TextWatcher {
	override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

	}

	override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

	}

	private var timer = Timer()
	private val DELAY: Long = 300 // Milliseconds


	override fun afterTextChanged(s: Editable?) {
		timer.cancel()
		timer = Timer()
		timer.schedule(
			object : TimerTask() {
				override fun run() {
					onItem.postValue(s.toString())
				}
			},
			DELAY
		)
	}
}