package com.example.tasklist.view.taskList

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tasklist.databinding.FragmentChangeTaskListBinding
import com.example.tasklist.dev.hideKeyboard
import com.example.tasklist.viewModel.taskList.ChangeTaskListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeTaskListFragment : BottomSheetDialogFragment() {
	private val viewModel: ChangeTaskListViewModel by viewModels()
	private val args: ChangeTaskListFragmentArgs by navArgs()
	private lateinit var binding: FragmentChangeTaskListBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentChangeTaskListBinding.inflate(inflater, container, false)

		binding.viewModel = viewModel
		binding.lifecycleOwner = viewLifecycleOwner

		viewModel.taskListId = args.taskListId

		binding.textInputEditText.requestFocus()

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		viewModel.onChangeTaskListClick.observe(viewLifecycleOwner) {
			viewModel.changeTaskListClick()
		}

		viewModel.onChangeTaskListFinish.observe(viewLifecycleOwner) {
			if (it)
				findNavController().popBackStack()
			else
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