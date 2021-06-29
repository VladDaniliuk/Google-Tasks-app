package com.example.tasklist.view.taskListList

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.tasklist.BR
import com.example.tasklist.R
import com.example.tasklist.databinding.FragmentCreateTaskListBinding
import com.example.tasklist.dev.hideKeyboard
import com.example.tasklist.view.base.BaseBottomSheetDialogFragment
import com.example.tasklist.viewModel.taskListList.CreateTaskListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateTaskListFragment(
	override val viewModelClass: Class<CreateTaskListViewModel> = CreateTaskListViewModel::class.java,
	override val layoutId: Int = R.layout.fragment_create_task_list,
	override val bindingVariableName: Int = BR.viewModel
) : BaseBottomSheetDialogFragment<FragmentCreateTaskListBinding, CreateTaskListViewModel>() {
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		binding.textInputEditText.requestFocus()

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