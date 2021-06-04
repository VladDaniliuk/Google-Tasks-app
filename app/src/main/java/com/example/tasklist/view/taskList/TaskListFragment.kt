package com.example.tasklist.view.taskList

import android.annotation.SuppressLint
import android.graphics.Color
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
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListFragment : Fragment() {

	private val viewModel: TaskListViewModel by viewModels()
	private val args: TaskListFragmentArgs by navArgs()
	private lateinit var binding: FragmentTaskListBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		sharedElementEnterTransition = MaterialContainerTransform().apply {
			scrimColor = Color.TRANSPARENT
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		postponeEnterTransition()
		binding = FragmentTaskListBinding.inflate(inflater, container, false)

		binding.viewModel = viewModel
		binding.lifecycleOwner = viewLifecycleOwner

		viewModel.parentId = args.parentId
		binding.root.transitionName = args.parentId

		binding.mainToolbar.inflateMenu(R.menu.menu_task_list)
		binding.mainToolbar.setOnMenuItemClickListener {
			if (it.itemId == R.id.delete) {
				setFragmentResult("requestKey", bundleOf("id" to viewModel.parentId))
				findNavController().popBackStack()
				return@setOnMenuItemClickListener true
			} else {
				findNavController().navigate(
					TaskListFragmentDirections.actionTaskListFragmentToChangeTaskListFragment(
						viewModel.parentId!!
					)
				)
				return@setOnMenuItemClickListener true
			}
		}

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		//postponeEnterTransition()
		binding.swipeRefresh.setOnRefreshListener {
			viewModel.fetchBase()
		}

		viewModel.onBaseClick.observe(viewLifecycleOwner) {
			findNavController().navigate(
				TaskListFragmentDirections.actionTaskListFragmentToTaskFragment(
					viewModel.parentId!!,
					it.first
				)
			)
		}

		viewModel.list.observe(viewLifecycleOwner) {
			val currentList = viewModel.adapter.currentList
			onList(it)
			if(currentList.isEmpty())
				startPostponedEnterTransition()
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
			completing + id + failed,
			Snackbar.LENGTH_SHORT
		).setAnchorView(binding.insertTask).show()
	}

	@SuppressLint("ShowToast")
	private fun showSnackBarDelete(id: String, isCompleted: Boolean) {
		Snackbar.make(
			requireView(),
			deleting + if (isCompleted) completed else failed,
			Snackbar.LENGTH_SHORT
		).setAction(cancel) {
			viewModel.deleteBase(id, false)
		}
			.setAnchorView(binding.insertTask).show()
	}

	@SuppressLint("ShowToast")
	private fun showSnackBarRestore(completed: Boolean) {
		Snackbar.make(
			requireView(),
			restore + if (completed) completed else failed,
			Snackbar.LENGTH_SHORT
		).setAnchorView(binding.insertTask).show()
	}

	companion object {
		const val completing = "Completing "
		const val failed = " failed"
		const val deleting = "Deleting "
		const val completed = " completed"
		const val cancel = "Cancel"
		const val restore = "Restore"
	}
}