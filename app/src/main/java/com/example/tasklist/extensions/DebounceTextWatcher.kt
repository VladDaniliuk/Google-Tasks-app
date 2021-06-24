package com.example.tasklist.extensions

import android.text.Editable
import android.text.TextWatcher
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class DebounceTextWatcher(private val callback: (String) -> Unit) : TextWatcher {

	private var userInputSubject = BehaviorSubject.create<String>().apply {
		distinctUntilChanged()
			.debounce(400, TimeUnit.MILLISECONDS)
			.subscribe { input: String ->
				Timber.v(input)
				callback(input)
			}
	}

	override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

	override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

	override fun afterTextChanged(s: Editable?) {
		userInputSubject.onNext(s.toString())
	}
}