package com.example.tasklist.view.adapter

import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView

class SwipeController : Callback() {
	var swipeBack = false

	override fun getMovementFlags(
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder
	): Int {
		return makeMovementFlags(0, LEFT or RIGHT)
	}

	override fun onMove(
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder,
		target: RecyclerView.ViewHolder
	): Boolean {
		return false
	}

	override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
	}

	override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
		if (swipeBack) {
			swipeBack = false
			return 0
		}
		return super.convertToAbsoluteDirection(flags, layoutDirection)
	}
}