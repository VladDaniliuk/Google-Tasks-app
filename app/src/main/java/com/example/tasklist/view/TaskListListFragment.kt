package com.example.tasklist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.tasklist.databinding.FragmentTaskListListBinding
import com.example.tasklist.viewModel.TaskListListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListListFragment : Fragment() {

	private val viewModel: TaskListListViewModel by viewModels()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
							  , savedInstanceState: Bundle?): View {
		val binding = FragmentTaskListListBinding.inflate(inflater, container, false)

		binding.viewModel = viewModel
		binding.lifecycleOwner = viewLifecycleOwner
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		viewModel.getTaskListList()
	}
}