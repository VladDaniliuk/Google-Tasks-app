package com.example.tasklist.view

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tasklist.databinding.FragmentCreateTaskBinding
import com.example.tasklist.dev.hideKeyboard
import com.example.tasklist.viewModel.CreateTaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateTaskFragment : BottomSheetDialogFragment() {
	private val viewModel: CreateTaskViewModel by viewModels()
	private val args: CreateTaskFragmentArgs by navArgs()
	private lateinit var binding: FragmentCreateTaskBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentCreateTaskBinding.inflate(inflater,container,false)

		binding.viewModel = viewModel
		binding.lifecycleOwner = viewLifecycleOwner

		viewModel.taskListId = args.taskListId

		binding.textInputEditText.requestFocus()

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		viewModel.onCreateBaseClick.observe(viewLifecycleOwner) {
			viewModel.onCreateBaseClick()
		}

		viewModel.onCreateBaseFinish.observe(viewLifecycleOwner) {
			findNavController().popBackStack()
		}

		viewModel.onCreateBaseError.observe(viewLifecycleOwner) {
			showErrorSnackBar()
		}
	}

	override fun onDismiss(dialog: DialogInterface) {
		super.onDismiss(dialog)
		binding.root.hideKeyboard()
	}

	@SuppressLint("ShowToast")
	private fun showErrorSnackBar() {
		Snackbar.make(
			requireView(),
			"Connection error",
			Snackbar.LENGTH_SHORT
		).setAnchorView(binding.root).show()
	}
}