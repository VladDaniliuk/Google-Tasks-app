package com.example.tasklist.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.databinding.FragmentTaskListBinding
import com.example.tasklist.view.adapter.SwipeController
import com.example.tasklist.view.itemModel.TaskItemModel
import com.example.tasklist.viewModel.TaskListViewModel
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
		super.onViewCreated(view, savedInstanceState)

		val simpleItemTouchCallback = object : SwipeController() {
			override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
				showDeletingSnackBar()
			}
		}

		val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
		itemTouchHelper.attachToRecyclerView(binding.listView)

		binding.swipeRefresh.setOnRefreshListener {
			viewModel.fetchTasks(args.parentId)
		}

		viewModel.onTaskClick.observe(viewLifecycleOwner) {
			viewModel
		}

		viewModel.list.observe(viewLifecycleOwner) {
			onList(it)
		}
	}

	private fun onList(it: List<TaskItemModel>) {
		viewModel.adapter.submitList(it)
	}

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