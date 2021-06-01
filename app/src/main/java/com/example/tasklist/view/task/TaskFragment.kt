package com.example.tasklist.view.task

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tasklist.databinding.FragmentTaskBinding
import com.example.tasklist.view.taskList.TaskListFragment
import com.example.tasklist.viewModel.task.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : Fragment() {
	private val args: TaskFragmentArgs by navArgs()
	private lateinit var binding: FragmentTaskBinding
	private val viewModel: TaskViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentTaskBinding.inflate(inflater, container, false)

		binding.viewModel = viewModel
		binding.lifecycleOwner = viewLifecycleOwner

		viewModel.id = Pair(args.taskListId, args.taskId)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		setFragmentResultListener(requestKey) { _, bundle ->
			bundle.getString(choice)?.let { choice ->
				when (choice) {
					delete -> findNavController().popBackStack()
					rename -> viewModel.fetchTask()
				}
			}
		}

		viewModel.onCompleteTaskClick.observe(viewLifecycleOwner) {
			viewModel.onCompleteTaskClick()
		}

		viewModel.onCompleteTaskError.observe(viewLifecycleOwner) {
			showSnackBarResult(it)
		}

		viewModel.task.observe(viewLifecycleOwner) {
			viewModel.taskAdapter.submitList(it.list)
		}

		viewModel.onTaskDelete.observe(viewLifecycleOwner) {
			setFragmentResult(requestKey, bundleOf(requestValue to viewModel.id?.second))
			findNavController().popBackStack()
		}

		viewModel.onTaskClick.observe(viewLifecycleOwner) {
			findNavController().navigate(
				TaskFragmentDirections.actionTaskFragmentSelf(
					viewModel.id!!.first,
					it
				)
			)
		}

		viewModel.onDeleteBaseClick.observe(viewLifecycleOwner) {
			viewModel.deleteSubTask(it, true)
		}

		viewModel.onDeleteBaseResult.observe(viewLifecycleOwner) {
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

		binding.swipeRefresh.setOnRefreshListener {
			viewModel.fetchTask()
		}
	}

	@SuppressLint("ShowToast")
	private fun showSnackBarResult(id: String) {
		Snackbar.make(
			requireView(),
			completing + id + failed,
			Snackbar.LENGTH_SHORT
		).setAnchorView(binding.completeTask).show()
	}

	@SuppressLint("ShowToast")
	private fun showSnackBarDelete(id: String, isCompleted: Boolean) {
		Snackbar.make(
			requireView(),
			TaskListFragment.deleting + if (isCompleted)
				TaskListFragment.completed
			else TaskListFragment.failed,
			Snackbar.LENGTH_SHORT
		).setAction(TaskListFragment.cancel) {
			viewModel.deleteSubTask(id, false)
		}
			.setAnchorView(binding.completeTask).show()
	}

	@SuppressLint("ShowToast")
	private fun showSnackBarRestore(completed: Boolean) {
		Snackbar.make(
			requireView(),
			TaskListFragment.restore + if (completed)
				TaskListFragment.completed
			else TaskListFragment.failed,
			Snackbar.LENGTH_SHORT
		).setAnchorView(binding.completeTask).show()
	}

	companion object {
		const val choice = "choice"
		const val completing = "Completing "
		const val delete = "Delete"
		const val failed = " failed"
		const val rename = "Rename"
		const val requestKey = "requestKey"
		const val requestValue = "id"
	}
}