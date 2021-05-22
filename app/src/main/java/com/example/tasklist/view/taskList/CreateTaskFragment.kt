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
import com.example.tasklist.databinding.FragmentCreateTaskBinding
import com.example.tasklist.dev.hideKeyboard
import com.example.tasklist.viewModel.taskList.CreateTaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

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
		binding = FragmentCreateTaskBinding.inflate(inflater, container, false)

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
			connectionError,
			Snackbar.LENGTH_SHORT
		).setAnchorView(binding.root).show()
	}

	@SuppressLint("SimpleDateFormat")
	fun showDatePicker() {
		val picker = MaterialDatePicker.Builder.datePicker()
			.setTitleText(selectDate)
			.build()

		picker.show(childFragmentManager, null)
		picker.addOnPositiveButtonClickListener {
			val inputFormat = SimpleDateFormat(dateFormat)
			viewModel.dueDate.postValue(inputFormat.format(picker.selection).toString())
		}
	}

	companion object Strings {
		const val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
		const val selectDate = "Select date"
		const val connectionError = "Connection error"
	}
}