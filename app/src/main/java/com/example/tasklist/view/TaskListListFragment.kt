package com.example.tasklist.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tasklist.R
import com.example.tasklist.viewModel.TaskListListViewModel

class TaskListListFragment : Fragment() {

	companion object {
		fun newInstance() = TaskListListFragment()
	}

	private lateinit var viewModel: TaskListListViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_task_list_list, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		viewModel = ViewModelProvider(this).get(TaskListListViewModel::class.java)
		// TODO: Use the ViewModel
	}

}