package com.example.tasklist.view.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper.Callback
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.R

open class SwipeController(private val context: Context) : Callback() {
	override fun getMovementFlags(
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder
	): Int {
		return makeMovementFlags(0, LEFT)
	}

	override fun onMove(
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder,
		target: RecyclerView.ViewHolder
	): Boolean {
		return false
	}

	override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {}

	override fun onChildDraw(
		canvas: Canvas,
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder,
		dX: Float,
		dY: Float,
		actionState: Int,
		isCurrentlyActive: Boolean
	) {
		if ((dX == 0.toFloat()) && !isCurrentlyActive) {
			super.onChildDraw(
				canvas, recyclerView, viewHolder, dX, dY, actionState,
				isCurrentlyActive
			)
			return
		}

		val itemView = viewHolder.itemView
		val paint = Paint()
		val background = RectF(
			itemView.left.toFloat(),
			itemView.top.toFloat(),
			itemView.right.toFloat(), itemView.bottom.toFloat()
		)
		val drawableDelete =
			ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_outline_24)

		paint.color = Color.RED

		drawableDelete!!.setBounds(
			(itemView.right - drawableDelete.intrinsicWidth * 1.5).toInt(),
			itemView.top + (itemView.height - drawableDelete.intrinsicHeight) / 2,
			itemView.right - drawableDelete.intrinsicWidth / 2,
			itemView.top +
					(itemView.height - drawableDelete.intrinsicHeight) / 2 +
					drawableDelete.intrinsicHeight
		)

		canvas.drawRoundRect(background, dpToPx(), dpToPx(), paint)

		drawableDelete.draw(canvas)

		super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
	}

	private fun dpToPx(): Float {
		return 8.4F * context.resources.displayMetrics.density
	}
}