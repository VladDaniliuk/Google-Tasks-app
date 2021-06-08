package com.example.tasklist.view.taskList

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tasklist.R
import com.example.tasklist.databinding.FragmentTaskListBinding
import com.example.tasklist.view.itemModel.TaskItemModel
import com.example.tasklist.viewModel.taskList.TaskListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListFragment : Fragment() {
	private val viewModel: TaskListViewModel by viewModels()
	private val args: TaskListFragmentArgs by navArgs()
	private lateinit var binding: FragmentTaskListBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentTaskListBinding.inflate(inflater, container, false)

		binding.viewModel = viewModel
		binding.lifecycleOwner = viewLifecycleOwner

		viewModel.parentId = args.parentId

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		binding.swipeRefresh.setOnRefreshListener {
			viewModel.fetchBase()
		}

		viewModel.onBaseClick.observe(viewLifecycleOwner) {
			findNavController().navigate(
				TaskListFragmentDirections.actionTaskListFragmentToTaskFragment(
					viewModel.parentId!!,
					it
				)
			)
		}

		viewModel.list.observe(viewLifecycleOwner) {
			onList(it)
		}

		viewModel.onTaskListDelete.observe(viewLifecycleOwner) {
			setFragmentResult(
				getString(R.string.request_key),
				bundleOf(getString(R.string.request_value) to viewModel.parentId)
			)
			findNavController().popBackStack()
		}

		viewModel.onTaskListEdit.observe(viewLifecycleOwner) {
			findNavController().navigate(
				TaskListFragmentDirections.actionTaskListFragmentToChangeTaskListFragment(
					viewModel.parentId!!
				)
			)
		}

		viewModel.onExecuteTaskResult.observe(viewLifecycleOwner) {
			showSnackBarResult(it)
			viewModel.adapter.notifyDataSetChanged()
		}

		viewModel.onDeleteBaseResult.observe(viewLifecycleOwner) {
			if (it.second) showSnackBarDelete(it.first, it.third)
			else showSnackBarRestore(it.third)
			if (!it.third) viewModel.adapter.notifyDataSetChanged()
		}

		viewModel.onCreateBaseClick.observe(viewLifecycleOwner) {
			findNavController().navigate(
				TaskListFragmentDirections.actionTaskListFragmentToCreateTaskFragment(
					viewModel.parentId!!, null
				)
			)
		}

		viewModel.onDeleteBaseClick.observe(viewLifecycleOwner) {
			viewModel.deleteBase(it, true)
		}
	}

	private fun onList(it: List<TaskItemModel>) {
		viewModel.adapter.submitList(it)
	}

	@SuppressLint("ShowToast")
	private fun showSnackBarResult(id: String) {
		Snackbar.make(
			requireView(),
			getString(R.string.completing_info, id, getString(R.string.failed)),
			Snackbar.LENGTH_SHORT
		).setAnchorView(binding.insertTask).show()
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
			viewModel.deleteBase(id, false)
		}
			.setAnchorView(binding.insertTask).show()
	}

	@SuppressLint("ShowToast")
	private fun showSnackBarRestore(completed: Boolean) {
		Snackbar.make(
			requireView(),
			getString(
				R.string.restoring_info,
				if (completed) getString(R.string.completed) else getString(R.string.failed)
			),
			Snackbar.LENGTH_SHORT
		).setAnchorView(binding.insertTask).show()
	}
}