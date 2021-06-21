package com.example.tasklist.extensions

import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StrikethroughSpan
import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tasklist.R
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.view.adapter.BaseItemAdapter
import com.example.tasklist.view.adapter.SwipeController
import com.example.tasklist.view.itemModel.BaseItemModel
import com.example.tasklist.view.itemModel.TaskItemModel
import com.example.tasklist.view.itemModel.TaskListItemModel
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber
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

@SuppressLint("SetTextI18n")
@BindingAdapter("isCompletedButton")
fun ExtendedFloatingActionButton.bindingSetChecked(isCompleted: String?) {
	this.setIconResource(
		if (isCompleted == "completed") R.drawable.ic_baseline_close_24
		else R.drawable.ic_baseline_done_24
	)
	this.text = (if (isCompleted == "completed") "Incomplete" else "Complete") + " task"
	this.extend()
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

@BindingAdapter("isDue")
fun TextView.bindingIsDue(due: String?) {
	if (due == null) {
		this.visibility = View.VISIBLE
	} else {
		this.visibility = View.GONE
	}
}

@BindingAdapter("itemTouchHelper", "onItemMoved")
fun RecyclerView.bindingItemTouchHelper(
	onItemAdapter: SingleLiveEvent<Pair<String, Boolean>>,
	onItemMoved: SingleLiveEvent<Pair<String, String?>>?
) {
	val adapter = adapter ?: return
	var startPos: Int? = null
	var startList: List<BaseItemModel> = emptyList()

	val taskSubject = BehaviorSubject.create<Pair<String, String?>>().apply {
		distinctUntilChanged()
			.subscribe {
				onItemMoved!!.postValue(it)
				startPos = null
			}
	}

	val simpleItemTouchCallback = object : SwipeController(this.context) {
		override fun onMove(
			recyclerView: RecyclerView,
			viewHolder: RecyclerView.ViewHolder,
			target: RecyclerView.ViewHolder
		): Boolean {
			return true
		}

		override fun onMoved(
			recyclerView: RecyclerView,
			viewHolder: RecyclerView.ViewHolder,
			fromPos: Int,
			target: RecyclerView.ViewHolder,
			toPos: Int,
			x: Int,
			y: Int
		) {
			if (startPos == null) {
				startList = (adapter as BaseItemAdapter<*, *>).currentList
			}
			startPos = startPos ?: fromPos

			(adapter as BaseItemAdapter<*, *>).onItemMove(fromPos, toPos)
		}

		override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
			val pair: Pair<String, Boolean> = when (adapter) {
				is ConcatAdapter -> Pair(
					(adapter.adapters[1] as BaseItemAdapter<*, *>)
						.currentList[viewHolder.bindingAdapterPosition].id,
					false
				)
				is BaseItemAdapter<*, *> -> {
					when (val itemModel = adapter.currentList[viewHolder.bindingAdapterPosition]) {
						is TaskItemModel -> Pair(itemModel.id, itemModel.deleted ?: false)
						is TaskListItemModel -> Pair(itemModel.id, false)
						else -> throw IllegalStateException("Position is not correct")
					}
				}
				else -> throw IllegalStateException("Position is not correct")
			}

			onItemAdapter.postValue(pair)
		}

		override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
			super.clearView(recyclerView, viewHolder)
			startPos?.let { startPos ->
				if ((adapter as BaseItemAdapter<*, *>)
						.currentList[viewHolder.bindingAdapterPosition] is TaskItemModel
				) {
					val toPos = viewHolder.bindingAdapterPosition
					Timber.v(startList[startPos].id)
					taskSubject.onNext(
						startList[startPos].id to when {
							startPos < toPos -> startList[toPos].id
							toPos == 0 -> null
							else -> startList[toPos - 1].id
						}
					)
				}
			}
			startPos = null
		}
	}

	val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
	itemTouchHelper.attachToRecyclerView(this)
}

@BindingAdapter("onClick")
fun RadioButton.bindingOnClick(onRadioButtonChoose: SingleLiveEvent<Int>) {
	this.setOnClickListener {
		onRadioButtonChoose.postValue(it.id)
	}
}