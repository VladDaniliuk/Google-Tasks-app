package com.example.tasklist.view.task

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tasklist.BR
import com.example.tasklist.R
import com.example.tasklist.databinding.FragmentTaskBinding
import com.example.tasklist.dev.themeColor
import com.example.tasklist.view.base.BaseFragment
import com.example.tasklist.viewModel.task.TaskViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import com.google.api.client.util.DateTime
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment(
	override val layoutId: Int = R.layout.fragment_task,
	override val bindingVariableName: Int = BR.viewModel,
	override val viewModelClass: Class<TaskViewModel> = TaskViewModel::class.java
) : BaseFragment<FragmentTaskBinding, TaskViewModel>() {
	private val args: TaskFragmentArgs by navArgs()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		enterTransition = MaterialFadeThrough()
		sharedElementEnterTransition = MaterialContainerTransform().apply {
			scrimColor = Color.TRANSPARENT
			setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
		}
		viewModel.id = Pair(args.taskListId, args.taskId)
	}

	@SuppressLint("SimpleDateFormat")
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		postponeEnterTransition()
		viewModel.onCompleteTaskClick.observe(viewLifecycleOwner) {
			viewModel.onCompleteTaskClick()
		}

		viewModel.onCompleteTaskError.observe(viewLifecycleOwner) {
			showSnackBarResult(it)
		}

		viewModel.task.observe(viewLifecycleOwner) {
			viewModel.taskAdapter.submitList(it.list)
			view.doOnPreDraw { startPostponedEnterTransition() }
		}

		viewModel.onTaskDelete.observe(viewLifecycleOwner) {
			viewModel.deleteTask()
		}

		viewModel.onDeleteTaskResult.observe(viewLifecycleOwner) {
			findNavController().popBackStack()
		}

		viewModel.onTaskClick.observe(viewLifecycleOwner) {
			exitTransition = MaterialElevationScale(false)
			reenterTransition = MaterialElevationScale(true)
			val extras = FragmentNavigatorExtras(it.second to it.first)
			findNavController().navigate(
				TaskFragmentDirections.actionTaskFragmentSelf(
					viewModel.id!!.first,
					it.first
				), extras
			)
		}

		viewModel.onDeleteBaseClick.observe(viewLifecycleOwner) {
			viewModel.deleteSubTask(it.first, !it.second)
		}

		viewModel.onDeleteSubTaskResult.observe(viewLifecycleOwner) {
			if (it.second) showSnackBarDelete(it.first, it.third)
			else showSnackBarRestore(it.third)
			if (!it.third) viewModel.adapter.notifyDataSetChanged()
		}

		viewModel.onTaskEdit.observe(viewLifecycleOwner) {
			findNavController().navigate(
				TaskFragmentDirections.actionTaskFragmentToChangeTaskFragment(
					viewModel.id!!.first,
					viewModel.id!!.second
				)
			)
		}

		viewModel.onAddSubTaskClick.observe(viewLifecycleOwner) {
			findNavController().navigate(
				TaskFragmentDirections.actionTaskFragmentToCreateTaskFragment(
					viewModel.id!!.first,
					viewModel.id!!.second
				)
			)
		}

		viewModel.onAddDueDateClick.observe(viewLifecycleOwner) {
			val picker = MaterialDatePicker.Builder.datePicker()
				.setTitleText(getString(R.string.select_date))
				.build()

			picker.show(childFragmentManager, null)
			picker.addOnPositiveButtonClickListener {
				picker.selection?.let {
					viewModel.changeTask(DateTime(it, 0).toString())
				}
			}
		}

		viewModel.onDeleteDueDateClick.observe(viewLifecycleOwner) {
			viewModel.changeTask("")
		}

		binding.swipeRefresh.setOnRefreshListener {
			viewModel.fetchTask()
		}

		viewModel.onNoteEdit.observe(viewLifecycleOwner) {
			viewModel.changeTask(notes = it)
		}
	}

	@SuppressLint("ShowToast")
	private fun showSnackBarResult(id: String) {
		Snackbar.make(
			requireView(),
			getString(R.string.completing_info, id, getString(R.string.failed)),
			Snackbar.LENGTH_SHORT
		).setAnchorView(binding.completeTask).show()
	}

	@SuppressLint("ShowToast")
	private fun showSnackBarDelete(id: String, isCompleted: Boolean) {
		Snackbar.make(
			requireView(),
			getString(
				R.string.deleting_info,
				if (isCompleted) getString(R.string.completed) else getString(R.string.failed)
			),
			Snackbar.LENGTH_SHORT
		).setAction(getString(R.string.cancel)) {
			viewModel.deleteSubTask(id, false)
		}
			.setAnchorView(binding.completeTask).show()
	}

	@SuppressLint("ShowToast")
	private fun showSnackBarRestore(complete: Boolean) {
		Snackbar.make(
			requireView(),
			getString(
				R.string.restoring_info,
				if (complete) getString(R.string.completed) else getString(R.string.failed)
			),
			Snackbar.LENGTH_SHORT
		).setAnchorView(binding.completeTask).show()
	}
}