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

		viewModel.onTaskListDelete.observe(viewLifecycleOwner) {
			setFragmentResult(requestKey, bundleOf(requestValue to viewModel.parentId))
			findNavController().popBackStack()
		}

		viewModel.onTaskListEdit.observe(viewLifecycleOwner) {
			findNavController().navigate(
				TaskListFragmentDirections.actionTaskListFragmentToChangeTaskListFragment(
					viewModel.parentId!!
				)
			)
		}
	}

	private fun onList(it: List<TaskItemModel>) {
		viewModel.adapter.submitList(it)
	}

	companion object {
		const val requestKey = "requestKey"
		const val requestValue = "id"
	}
}