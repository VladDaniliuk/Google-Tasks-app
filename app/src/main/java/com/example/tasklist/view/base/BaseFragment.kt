package com.example.tasklist.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tasklist.BR


abstract class BaseFragment<V : ViewDataBinding, VM : ViewModel> : Fragment() {
	abstract val layoutId: Int
	abstract val viewModelClass: Class<VM>
	lateinit var binding: V

	val viewModel by lazy { ViewModelProvider(this).get(viewModelClass) }

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate(
			inflater,
			layoutId,
			container,
			false
		)
		binding.setVariable(BR.viewModel, viewModel)
		binding.lifecycleOwner = viewLifecycleOwner
		return binding.root
	}
}