package com.example.tasklist.view

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseFragment<V : ViewDataBinding, VM : ViewModel> : Fragment() {
	lateinit var binding: V
	abstract val viewModelClass: Class<VM>

	val viewModel by lazy { ViewModelProvider(this).get(viewModelClass) }
}