package com.example.tasklist.view.taskListList

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.tasklist.R
import com.example.tasklist.databinding.FragmentTaskListListBinding
import com.example.tasklist.extensions.FabExtendingOnScrollListener
import com.example.tasklist.view.base.BaseFragment
import com.example.tasklist.view.itemModel.TaskListItemModel
import com.example.tasklist.viewModel.taskListList.TaskListListViewModel
import com.example.tasklist.worker.FetchWorker
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class TaskListListFragment(
	override val layoutId: Int = R.layout.fragment_task_list_list,
	override val viewModelClass: Class<TaskListListViewModel> = TaskListListViewModel::class.java
) : BaseFragment<FragmentTaskListListBinding, TaskListListViewModel>() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		enterTransition = MaterialFadeThrough()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.listView.addOnScrollListener(FabExtendingOnScrollListener(binding.insertTaskList))

		context?.let { context ->
			val uploadWorkRequest: WorkRequest =
				PeriodicWorkRequest.Builder(FetchWorker::class.java, 1, TimeUnit.MINUTES)
					.build()
			WorkManager.getInstance(context).enqueue(uploadWorkRequest)
		}

		postponeEnterTransition()
		view.doOnPreDraw { startPostponedEnterTransition() }

		setFragmentResultListener(getString(R.string.request_key)) { _, bundle ->
			bundle.getString(getString(R.string.request_value))?.let { id ->
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
			viewModel.deleteBase(it.first, !it.second)
		}

		viewModel.onCreateBaseClick.observe(viewLifecycleOwner) {
			findNavController().navigate(R.id.action_taskListListFragment_to_createTaskListFragment)
		}

		viewModel.onDeleteBaseResult.observe(viewLifecycleOwner) {
			showSnackBarResult(it)
			if (!it) viewModel.adapter.notifyDataSetChanged()
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
	}

	@SuppressLint("ShowToast")
	private fun showSnackBarResult(completed: Boolean) {
		Snackbar.make(
			requireView(),
			getString(
				R.string.deleting_info,
				if (completed) {
					getString(R.string.completed)
				} else {
					getString(R.string.failed)
				}
			), Snackbar.LENGTH_SHORT
		).setAnchorView(binding.insertTaskList).show()
	}
}
