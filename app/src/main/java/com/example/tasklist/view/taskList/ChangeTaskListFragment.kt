package com.example.tasklist.view.taskList

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tasklist.BR
import com.example.tasklist.R
import com.example.tasklist.databinding.FragmentChangeTaskListBinding
import com.example.tasklist.dev.hideKeyboard
import com.example.tasklist.view.base.BaseBottomSheetDialogFragment
import com.example.tasklist.viewModel.taskList.ChangeTaskListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeTaskListFragment(
	override val layoutId: Int = R.layout.fragment_change_task_list,
	override val bindingVariableName: Int = BR.viewModel,
	override val viewModelClass: Class<ChangeTaskListViewModel> =
		ChangeTaskListViewModel::class.java
) : BaseBottomSheetDialogFragment<FragmentChangeTaskListBinding, ChangeTaskListViewModel>() {
	private val args: ChangeTaskListFragmentArgs by navArgs()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel.taskListId = args.taskListId
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		binding.textInputEditText.requestFocus()

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