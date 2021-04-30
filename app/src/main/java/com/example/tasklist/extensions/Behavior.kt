package com.example.tasklist.extensions

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton


class Behavior : CoordinatorLayout.Behavior<ExtendedFloatingActionButton> {

	private val STATE_SCROLLED_DOWN = 1
	private val STATE_SCROLLED_UP = 2

	private var currentState = STATE_SCROLLED_UP
	private val currentAnimator: ViewPropertyAnimator? = null

	constructor() : super()

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

	override fun onStartNestedScroll(
		coordinatorLayout: CoordinatorLayout,
		child: ExtendedFloatingActionButton,
		directTargetChild: View,
		target: View,
		nestedScrollAxes: Int,
		type: Int
	): Boolean {
		return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
	}

	override fun onNestedScroll(
		coordinatorLayout: CoordinatorLayout,
		child: ExtendedFloatingActionButton,
		target: View,
		dxConsumed: Int,
		dyConsumed: Int,
		dxUnconsumed: Int,
		dyUnconsumed: Int,
		type: Int,
		consumed: IntArray
	) {
		if (dyConsumed > 0) {
			slideDown(child)
		} else if (dyConsumed < 0) {
			slideUp(child)
		}
	}

	private fun slideUp(child: ExtendedFloatingActionButton) {
		if (currentState == STATE_SCROLLED_UP) {
			return
		}
		if (currentAnimator != null) {
			currentAnimator.cancel()
			child.clearAnimation()
		}
		currentState = STATE_SCROLLED_UP
		child.extend()
	}

	private fun slideDown(child: ExtendedFloatingActionButton) {
		if (currentState == STATE_SCROLLED_DOWN) {
			return
		}
		if (currentAnimator != null) {
			currentAnimator.cancel()
			child.clearAnimation()
		}
		currentState = STATE_SCROLLED_DOWN
		child.shrink()
	}
}