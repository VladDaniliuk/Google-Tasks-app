package com.example.tasklist.view

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
import com.example.tasklist.viewModel.TaskListViewModel
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

		binding.mainToolbar.inflateMenu(R.menu.menu_task_list)
		binding.mainToolbar.setOnMenuItemClickListener {
			if (it.itemId == R.id.delete) {
				setFragmentResult("requestKey", bundleOf("id" to viewModel.parentId))
				findNavController().popBackStack()
				return@setOnMenuItemClickListener true
			} else {
				findNavController().navigate(R.id.action_taskListFragment_to_changeTaskListFragment)
				return@setOnMenuItemClickListener true
			}
		}

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.swipeRefresh.setOnRefreshListener {
			viewModel.fetchTasks(args.parentId)
		}

		viewModel.onTaskClick.observe(viewLifecycleOwner) {
			viewModel
		}

		viewModel.list.observe(viewLifecycleOwner) {
			onList(it)
		}

		viewModel.onItemAdapter.observe(viewLifecycleOwner) {
		}
	}

	private fun onList(it: List<TaskItemModel>) {
		viewModel.adapter.submitList(it)
	}
}