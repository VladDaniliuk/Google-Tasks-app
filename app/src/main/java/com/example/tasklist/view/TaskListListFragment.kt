package com.example.tasklist.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasklist.BR
import com.example.tasklist.R
import com.example.tasklist.databinding.FragmentTaskListListBinding
import com.example.tasklist.databinding.LayoutTaskListBinding
import com.example.tasklist.view.itemModel.TaskListItemModel
import com.example.tasklist.viewModel.TaskListListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListListFragment : Fragment() {

	private val viewModel: TaskListListViewModel by viewModels()
	private lateinit var binding: FragmentTaskListListBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		binding = FragmentTaskListListBinding.inflate(inflater, container, false)

		binding.viewModel = viewModel
		binding.lifecycleOwner = viewLifecycleOwner
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.listView.layoutManager =
			LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)

		viewModel.getTaskListListEvent.observe(viewLifecycleOwner) {
			onList()
		}

		viewModel.onCreateTaskListClick.observe(viewLifecycleOwner) {
			onCreateTaskListClick()
		}

		viewModel.getTaskListList()
	}

	private fun onList() {
		val adapter = ListAdapter<TaskListItemModel, LayoutTaskListBinding>(
			BR.model,
			R.layout.layout_task_list,
			viewModel.list
		)
		binding.listView.adapter = adapter
	}

	private fun onCreateTaskListClick() {
		Log.d("CREATE", "create")
	}
}