package com.example.tasklist.view.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.R
import com.example.tasklist.view.itemModel.TaskItemModel

open class SwipeController(
	private val context: Context
) : Callback() {
	override fun getMovementFlags(
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder
	): Int {
		return when (recyclerView.adapter) {
			is ConcatAdapter -> makeMovementFlags(
				0,
				if (viewHolder is BaseItemAdapter<*, *>.ViewHolder) LEFT else 0
			)
			else ->
				if ((recyclerView.adapter as BaseItemAdapter<*, *>)
						.currentList[viewHolder.bindingAdapterPosition] is TaskItemModel
				) makeMovementFlags(UP or DOWN, LEFT) else makeMovementFlags(0, LEFT)
		}
	}

	override fun onMove(
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder,
		target: RecyclerView.ViewHolder
	): Boolean {
		return false
	}

	override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {}

	override fun canDropOver(
		recyclerView: RecyclerView,
		current: RecyclerView.ViewHolder,
		target: RecyclerView.ViewHolder
	): Boolean {
		return super.canDropOver(recyclerView, current, target)
	}

	override fun onChildDraw(
		canvas: Canvas,
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder,
		dX: Float,
		dY: Float,
		actionState: Int,
		isCurrentlyActive: Boolean
	) {
		if ((dX == 0.toFloat()) && !isCurrentlyActive || dY != 0.toFloat()) {
			super.onChildDraw(
				canvas, recyclerView, viewHolder, dX, dY, actionState,
				isCurrentlyActive
			)
			return
		}

		val itemView = viewHolder.itemView
		lateinit var drawableDelete: Drawable
		val paint = Paint()
		val background = RectF(
			itemView.left.toFloat(),
			itemView.top.toFloat(),
			itemView.right.toFloat(), itemView.bottom.toFloat()
		)

		drawableDelete =
			ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_outline_24)!!
		paint.color = Color.RED

		((viewHolder as BaseItemAdapter<*, *>.ViewHolder)
			.baseItem as? TaskItemModel)?.deleted?.let {
			if (it) {
				drawableDelete = ContextCompat.getDrawable(
					context,
					R.drawable.ic_baseline_restore_from_trash_24
				)!!
				paint.color = ContextCompat.getColor(context, R.color.green)
			}
		}

		drawableDelete.setBounds(
			(itemView.right - drawableDelete.intrinsicWidth * 1.5).toInt(),
			itemView.top + (itemView.height - drawableDelete.intrinsicHeight) / 2,
			itemView.right - drawableDelete.intrinsicWidth / 2,
			itemView.top +
					(itemView.height - drawableDelete.intrinsicHeight) / 2 +
					drawableDelete.intrinsicHeight
		)

		canvas.drawRoundRect(background, dpToPx(), dpToPx(), paint)

		drawableDelete.draw(canvas)

		super.onChildDraw(
			canvas,
			recyclerView,
			viewHolder,
			dX,
			dY,
			actionState,
			isCurrentlyActive
		)
	}

	private fun dpToPx(): Float {
		return 8.4F * context.resources.displayMetrics.density
	}
}