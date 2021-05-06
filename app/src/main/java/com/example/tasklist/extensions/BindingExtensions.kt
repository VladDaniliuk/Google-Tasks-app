package com.example.tasklist.extensions

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StrikethroughSpan
import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tasklist.view.itemModel.TaskItemModel
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

@BindingAdapter("isCompletedText", "text")
fun TextView.bindingSetChecked(isCompleted: String, title: String) {
	if (isCompleted == "completed") {
		val spannable = SpannableStringBuilder(title)
		spannable.setSpan(
			StrikethroughSpan(),
			0, // start
			spannable.length, // end
			Spannable.SPAN_EXCLUSIVE_INCLUSIVE
		)
		this.text = spannable
	} else {
		this.text = title
	}
}

@BindingAdapter("isCompleted")
fun CheckBox.bindingSetChecked(isCompleted: String) {
	this.isChecked = isCompleted == "completed"
}

@BindingAdapter("hasSubTask")
fun ImageButton.bindingHasSubTask(list: List<TaskItemModel>) {
	this.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE
}

@BindingAdapter("isSubTaskVisible")
fun ImageButton.bindingIsSubtaskVisible(isSubTaskVisible: Int) {
	this.rotation = if (isSubTaskVisible == View.VISIBLE) 180F else 0F
}