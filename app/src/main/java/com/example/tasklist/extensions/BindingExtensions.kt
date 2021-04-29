package com.example.tasklist.extensions

import android.view.View
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