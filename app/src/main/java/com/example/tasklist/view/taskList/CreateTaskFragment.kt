package com.example.tasklist.view.taskList

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tasklist.BR
import com.example.tasklist.R
import com.example.tasklist.databinding.FragmentCreateTaskBinding
import com.example.tasklist.dev.hideKeyboard
import com.example.tasklist.view.base.BaseBottomSheetDialogFragment
import com.example.tasklist.viewModel.taskList.CreateTaskViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.api.client.util.DateTime
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateTaskFragment(
	override val viewModelClass: Class<CreateTaskViewModel> = CreateTaskViewModel::class.java,
	override val layoutId: Int = R.layout.fragment_create_task,
	override val bindingVariableName: Int = BR.viewModel
) : BaseBottomSheetDialogFragment<FragmentCreateTaskBinding, CreateTaskViewModel>() {
	private val args: CreateTaskFragmentArgs by navArgs()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel.taskListId = args.taskListId
		viewModel.taskParentId = args.parentId
	}

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

		viewModel.setDateClick.observe(viewLifecycleOwner) {
			showDatePicker()
		}

		binding.chip.setOnCloseIconClickListener {
			viewModel.dueDate.postValue(null)
		}

		binding.chip.setOnClickListener {
			showDatePicker()
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
			getString(R.string.connection_error),
			Snackbar.LENGTH_SHORT
		).setAnchorView(binding.root).show()
	}

	@SuppressLint("SimpleDateFormat")
	fun showDatePicker() {
		val picker = MaterialDatePicker.Builder.datePicker()
			.setTitleText(getString(R.string.select_date))
			.build()

		picker.show(childFragmentManager, null)
		picker.addOnPositiveButtonClickListener {
			viewModel.dueDate.postValue(DateTime(it, 0).toStringRfc3339())
		}
	}
}