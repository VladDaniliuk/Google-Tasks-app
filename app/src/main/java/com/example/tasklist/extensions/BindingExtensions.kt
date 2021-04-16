package com.example.tasklist.extensions

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("isVisible")
fun setVisibility(view: View, isVisible: Boolean) {
	if (isVisible) {
		view.visibility = View.VISIBLE
	} else {
		view.visibility = View.GONE
	}
}
