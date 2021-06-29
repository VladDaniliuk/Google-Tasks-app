package com.example.tasklist.view.taskList

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
class CreateTaskFragment :
	BaseBottomSheetDialogFragment<FragmentCreateTaskBinding, CreateTaskViewModel>() {
	private val args: CreateTaskFragmentArgs by navArgs()

	override val viewModelClass: Class<CreateTaskViewModel> = CreateTaskViewModel::class.java

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentCreateTaskBinding.inflate(inflater, container, false)

		binding.viewModel = viewModel
		binding.lifecycleOwner = viewLifecycleOwner

		viewModel.taskListId = args.taskListId
		viewModel.taskParentId = args.parentId

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