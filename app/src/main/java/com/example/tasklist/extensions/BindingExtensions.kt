package com.example.tasklist.extensions

import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StrikethroughSpan
import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.view.adapter.BaseItemAdapter
import com.example.tasklist.view.adapter.SwipeController
import com.example.tasklist.view.itemModel.TaskItemModel
import com.google.android.material.chip.Chip
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("isVisible")
fun View.setVisibility(isVisible: Boolean) {
	if (isVisible) {
		this.visibility = View.VISIBLE
	} else {
		this.visibility = View.INVISIBLE
	}
}

@BindingAdapter("isRefreshing")
fun SwipeRefreshLayout.bindingSetRefreshing(isRefreshing: Boolean) {
	this.isRefreshing = isRefreshing
}

@BindingAdapter("isCompletedText", "text")
fun TextView.bindingSetChecked(isCompleted: String, title: String) {
	val spannable = SpannableStringBuilder(title)
	if (isCompleted == "completed") {
		spannable.setSpan(
			StrikethroughSpan(),
			0, // start
			spannable.length, // end
			Spannable.SPAN_EXCLUSIVE_INCLUSIVE
		)
		this.text = spannable
	} else {
		spannable.removeSpan(StrikethroughSpan())
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

@SuppressLint("SimpleDateFormat")
@BindingAdapter("isDue")
fun Chip.bindingIsDue(due: String?) {
	if (due == null) {
		this.visibility = View.GONE
	} else {
		val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
		val outputFormat = SimpleDateFormat("dd MMMM, yyyy")
		val date: Date = inputFormat.parse(due)!!

		this.visibility = View.VISIBLE
		this.text = outputFormat.format(date)
	}
}

@BindingAdapter("itemTouchHelper")
fun RecyclerView.bindingItemTouchHelper(onItemAdapter: SingleLiveEvent<String>) {
	val simpleItemTouchCallback = object : SwipeController(this.context) {
		override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
			onItemAdapter.postValue(
				(adapter as BaseItemAdapter<*, *>).currentList[viewHolder.adapterPosition].id
			)
		}
	}

	val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
	itemTouchHelper.attachToRecyclerView(this)
}