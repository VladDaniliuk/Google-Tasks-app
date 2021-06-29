package com.example.tasklist.view.task

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tasklist.databinding.FragmentChangeTaskBinding
import com.example.tasklist.dev.hideKeyboard
import com.example.tasklist.view.base.BaseBottomSheetDialogFragment
import com.example.tasklist.view.adapter.TaskListAdapter
import com.example.tasklist.viewModel.task.ChangeTaskViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeTaskFragment :
	BaseBottomSheetDialogFragment<FragmentChangeTaskBinding, ChangeTaskViewModel>() {
	private val args: ChangeTaskFragmentArgs by navArgs()

	override val viewModelClass: Class<ChangeTaskViewModel> = ChangeTaskViewModel::class.java

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentChangeTaskBinding.inflate(inflater, container, false)

		binding.viewModel = viewModel
		binding.lifecycleOwner = viewLifecycleOwner

		viewModel.id = Pair(args.taskListId, args.taskId)

		binding.textInputEditText.requestFocus()

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		viewModel.taskListList.observe(viewLifecycleOwner) { list ->
			val adapter = TaskListAdapter(
				requireContext(),
				android.R.layout.simple_dropdown_item_1line,
				list
			)
			binding.autoCompleteTextView.setAdapter(adapter)

			val currentTaskList = viewModel.taskListList
				.value?.filter {
					it.id == viewModel.id?.first
				}?.get(0)
			adapter.setItemSelected(viewModel.taskListList.value?.indexOf(currentTaskList) ?: 0)

			binding.autoCompleteTextView.setText(currentTaskList.toString(), false)
			binding.autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
				adapter.setItemSelected(position)
			}
		}

		viewModel.onChangeTaskClickResult.observe(viewLifecycleOwner) {
			showSnackBar(it)
		}

		viewModel.onChangeTaskClick.observe(viewLifecycleOwner) {
			val taskList = (binding.autoCompleteTextView.adapter as TaskListAdapter).selectedItem
			taskList?.let {
				if (viewModel.task.value?.parentId == it.id) {
					viewModel.changeTask()
				} else {
					viewModel.changeTask(it.id)
				}
			}

		}
	}

	override fun onDismiss(dialog: DialogInterface) {
		super.onDismiss(dialog)
		binding.root.hideKeyboard()
	}

	@SuppressLint("ShowToast")
	private fun showSnackBar(it: Pair<Boolean, Boolean>) {
		Snackbar.make(
			requireView(),
			if (it.second) ok else connectionError,
			Snackbar.LENGTH_SHORT
		).setAnchorView(binding.root).show()

		if (it.second) {
			if (it.first)
				setFragmentResult(changeTaskRequestKey, bundleOf(choice to delete))
			else
				setFragmentResult(changeTaskRequestKey, bundleOf(choice to rename))
			findNavController().popBackStack()
		}
	}

	companion object {
		const val choice = "choice"
		const val connectionError = "Connection error"
		const val delete = "Delete"
		const val ok = "Ok"
		const val rename = "Rename"
		const val changeTaskRequestKey = "changeTaskRequestKey"
	}
}