package com.example.tasklist.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasklist.BR
import com.example.tasklist.R
import com.example.tasklist.databinding.FragmentTaskListBinding
import com.example.tasklist.databinding.LayoutTaskBinding
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

		binding.listView.layoutManager =
			LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)

		binding.swipeRefresh.setOnRefreshListener {
			viewModel.fetchTasks(args.parentId)
		}

		viewModel.list.observe(viewLifecycleOwner) {
			onList(it)
		}
	}

	private fun onList(it: List<TaskItemModel>) {
		val adapter = ListAdapter<TaskItemModel, LayoutTaskBinding>(
			BR.model,
			R.layout.layout_task,
			it
		)
		binding.listView.adapter = adapter
	}
}