package com.example.tasklist.view.taskListList

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.tasklist.R
import com.example.tasklist.databinding.FragmentTaskListListBinding
import com.example.tasklist.view.itemModel.TaskListItemModel
import com.example.tasklist.viewModel.taskListList.TaskListListViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListListFragment : Fragment() {

	private val viewModel: TaskListListViewModel by viewModels()
	private lateinit var binding: FragmentTaskListListBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enterTransition = MaterialFadeThrough()
	}

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
				viewModel.deleteBase(id)
			}
		}

		binding.swipeRefresh.setOnRefreshListener {
			viewModel.fetchBase()
		}

		viewModel.list.observe(viewLifecycleOwner) {
			onList(it)
		}

		viewModel.onBaseClick.observe(viewLifecycleOwner) {
			navigateToTaskList(it.first, it.second)
		}

		viewModel.onDeleteBaseClick.observe(viewLifecycleOwner) {
			viewModel.deleteBase(it)
		}

		viewModel.onCreateBaseClick.observe(viewLifecycleOwner) {
			findNavController().navigate(R.id.action_taskListListFragment_to_createTaskListFragment)
		}

		viewModel.onDeleteBaseResult.observe(viewLifecycleOwner) {
			showSnackBarResult(it.first, it.second)
			if (!it.second) viewModel.adapter.notifyDataSetChanged()
		}
	}

	private fun navigateToTaskList(id: String, view: View) {
		exitTransition = MaterialElevationScale(false)
		reenterTransition = MaterialElevationScale(true)
		val extras = FragmentNavigatorExtras(view to "shared_element")
		findNavController().navigate(
			TaskListListFragmentDirections.actionTaskListListFragmentToTaskListFragment(
				id
			), extras
		)
	}

	private fun onList(it: List<TaskListItemModel>) {
		viewModel.adapter.submitList(it)
		/*(view?.parent as? ViewGroup)?.doOnPreDraw {
			startPostponedEnterTransition()
		}*/
	}

	@SuppressLint("ShowToast")
	private fun showSnackBarResult(id: String, completed: Boolean) {
		Snackbar.make(
			requireView(), "Deleting ${
				(viewModel.list.value?.filter {
					it.id == id
				}?.toList()?.get(0)?.title)
			}" + if (completed) {
				" completed"
			} else {
				" failed"
			}, Snackbar.LENGTH_SHORT
		).setAnchorView(binding.insertTaskList).show()
	}
}