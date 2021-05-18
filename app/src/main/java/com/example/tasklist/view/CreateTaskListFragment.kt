package com.example.tasklist.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tasklist.databinding.FragmentCreateTaskListBinding
import com.example.tasklist.dev.hideKeyboard
import com.example.tasklist.viewModel.CreateTaskListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

		binding.textInputEditText.requestFocus()

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		viewModel.onCreateTaskListClick.observe(viewLifecycleOwner) {
			viewModel.createTaskListClick()
		}

		viewModel.onCreateTaskListFinish.observe(viewLifecycleOwner) {
			findNavController().popBackStack()
		}

	}

	override fun onDismiss(dialog: DialogInterface) {
		super.onDismiss(dialog)
		binding.root.hideKeyboard()
	}
}