package com.example.tasklist.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import com.example.tasklist.databinding.FragmentCreateTaskListBinding
import com.example.tasklist.viewModel.CreateTaskListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CreateTaskListFragment : BottomSheetDialogFragment() {

	private val viewModel: CreateTaskListViewModel by viewModels()
	private lateinit var binding: FragmentCreateTaskListBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentCreateTaskListBinding.inflate(inflater, container, false)

		binding.viewModel = viewModel
		binding.lifecycleOwner = viewLifecycleOwner

		binding.textInputEditText.requestFocus()//not good

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {//not good
		val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
	}

	override fun onDestroy() {//not good
		super.onDestroy()
		try {
			(context as Activity).window.setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
			)
			if ((context as Activity).currentFocus != null
				&& (context as Activity).currentFocus!!.windowToken != null
			) {
				((context as Activity)
					.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
					.hideSoftInputFromWindow(
						(context as Activity).currentFocus!!.windowToken, 0
					)
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
}