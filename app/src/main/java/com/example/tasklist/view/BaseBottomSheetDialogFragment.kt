package com.example.tasklist.view

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment<V : ViewDataBinding, VM : ViewModel> :
	BottomSheetDialogFragment() {

	lateinit var binding: V
	abstract val viewModelClass: Class<VM>

	val viewModel by lazy { ViewModelProvider(this).get(viewModelClass) }
}