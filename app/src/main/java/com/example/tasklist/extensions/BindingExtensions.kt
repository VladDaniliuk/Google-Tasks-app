package com.example.tasklist.extensions

import android.graphics.Paint
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.common.SignInButton

@BindingAdapter("onClick")
fun SignInButton.bindingOnClick(onClick: View.OnClickListener) {
	this.setOnClickListener(onClick)
}

@BindingAdapter("isVisible")
fun setVisibility(view: View, isVisible: Boolean) {
	if (isVisible) {
		view.visibility = View.VISIBLE
	} else {
		view.visibility = View.GONE
	}
}

@BindingAdapter("isRefreshing")
fun SwipeRefreshLayout.bindingSetRefreshing(isRefreshing: Boolean) {
	this.isRefreshing = isRefreshing
}

@BindingAdapter("isCompletedText")
fun TextView.bindingSetChecked(isCompleted: String) {
	if (isCompleted == "completed") {
		this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
	} else {
		this.paintFlags = this.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG
	}
}

@BindingAdapter("isCompleted")
fun CheckBox.bindingSetChecked(isCompleted: String) {
	this.isChecked = isCompleted == "completed"
}
