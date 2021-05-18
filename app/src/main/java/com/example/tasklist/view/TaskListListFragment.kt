package com.example.tasklist.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tasklist.R
import com.example.tasklist.databinding.FragmentTaskListListBinding
import com.example.tasklist.view.itemModel.TaskListItemModel
import com.example.tasklist.viewModel.TaskListListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListListFragment : Fragment() {

	private val viewModel: TaskListListViewModel by viewModels()
	private lateinit var binding: FragmentTaskListListBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentTaskListListBinding.inflate(inflater, container, false)

		binding.viewModel = viewModel
		binding.lifecycleOwner = viewLifecycleOwner
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setFragmentResultListener("requestKey") { _, bundle ->
			bundle.getString("id")?.let { id ->
				viewModel.list.value?.filter {
					it.id == id
				}?.toList()?.get(0)?.clickable = false
				viewModel.deleteTaskList(id)
			}
		}

		binding.swipeRefresh.setOnRefreshListener {
			viewModel.fetchTaskLists()
		}

		viewModel.list.observe(viewLifecycleOwner) {
			onList(it)
		}

		viewModel.onTaskListClick.observe(viewLifecycleOwner) {
			navigateToTaskList(it)
		}

		viewModel.onDeleteTaskListClick.observe(viewLifecycleOwner) {
			viewModel.deleteTaskList(it)
		}

		viewModel.onCreateTaskListClick.observe(viewLifecycleOwner) {
			findNavController().navigate(R.id.action_taskListListFragment_to_createTaskListFragment)
		}

		viewModel.onDeleteTaskListError.observe(viewLifecycleOwner) {
			showErrorSnackBar(it)
			viewModel.adapter.notifyDataSetChanged()
		}

		viewModel.onDeleteTaskListComplete.observe(viewLifecycleOwner) {
			showCompleteSnackBar(it)
		}
	}

	private fun navigateToTaskList(id: String) {
		findNavController()
			.navigate(
				TaskListListFragmentDirections
					.actionTaskListListFragmentToTaskListFragment(id)
			)
	}

	private fun onList(it: List<TaskListItemModel>) {
		viewModel.adapter.submitList(it)
	}

	@SuppressLint("ShowToast")
	private fun showErrorSnackBar(position: String) {
		Snackbar.make(
			requireView(),
			"Deleting ${
				(viewModel.list.value?.filter {
					it.id == position
				}?.toList()?.get(0)?.title)
			} failed",
			Snackbar.LENGTH_SHORT
		).setAnchorView(binding.insertTaskList).show()
	}

	@SuppressLint("ShowToast")
	private fun showCompleteSnackBar(position: String) {
		Snackbar.make(
			requireView(),
			"Deleting ${
				(viewModel.list.value?.filter {
					it.id == position
				}?.toList()?.get(0)?.title)
			} completed",
			Snackbar.LENGTH_SHORT
		).setAnchorView(binding.insertTaskList).show()
	}
}