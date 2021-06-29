package com.example.tasklist.dev

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.use

fun View.hideKeyboard() {
	val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
	imm.hideSoftInputFromWindow(this.windowToken, 0)
}

@ColorInt
fun Context.themeColor(
	@AttrRes themeAttrId: Int
): Int {
	return obtainStyledAttributes(
		intArrayOf(themeAttrId)
	).use {
		it.getColor(0, Color.MAGENTA)
	}
}

fun Context.dpToPx(dp: Float): Float {
	return dp * resources.displayMetrics.density
}