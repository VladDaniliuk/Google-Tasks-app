package com.example.tasklist.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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

		binding.swipeRefresh.setOnRefreshListener {
			viewModel.fetchTaskLists()
		}

		viewModel.list.observe(viewLifecycleOwner) {
			onList(it)
		}

		viewModel.onTaskListClick.observe(viewLifecycleOwner) {
			navigateToTaskList(it)
		}

		viewModel.onItemAdapter.observe(viewLifecycleOwner) {
			showDeletingSnackBar()
		}

		viewModel.onCreateTaskListClick.observe(viewLifecycleOwner) {
			findNavController().navigate(R.id.action_taskListListFragment_to_createTaskListFragment)
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
	private fun showDeletingSnackBar() {
		val snackBar =
			Snackbar.make(requireView(), "Deleting list of tasks...", Snackbar.LENGTH_LONG)
				.setAction("Cancel") {
					Toast.makeText(requireContext(), "Deleted canceled", Toast.LENGTH_LONG)
						.show()
				}.setAnchorView(binding.insertTaskList)
				.addCallback(object : Snackbar.Callback() {
					override fun onShown(sb: Snackbar?) {
						super.onShown(sb)
					}

					override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
						if (event == DISMISS_EVENT_TIMEOUT) {
							Toast.makeText(
								requireContext(),
								"Deleted successfully",
								Toast.LENGTH_LONG
							)
								.show()
						}
					}
				})
		snackBar.show()
	}
}