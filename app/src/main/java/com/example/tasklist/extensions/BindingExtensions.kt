package com.example.tasklist.extensions

import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.gms.common.SignInButton

@BindingAdapter("onClick")
fun SignInButton.bindingOnClick(onClick: View.OnClickListener) {
    this.setOnClickListener(onClick)
}