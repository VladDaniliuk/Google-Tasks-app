package com.example.tasklist.view.task

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.tasklist.databinding.FragmentTaskBinding
import com.example.tasklist.viewModel.task.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : Fragment() {
	private val args: TaskFragmentArgs by navArgs()
	private lateinit var binding: FragmentTaskBinding
	private val viewModel: TaskViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentTaskBinding.inflate(inflater, container, false)

		binding.viewModel = viewModel
		binding.lifecycleOwner = viewLifecycleOwner

		viewModel.id = Pair(args.taskListId,args.taskId)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		viewModel.onCompleteTaskClick.observe(viewLifecycleOwner) {
			viewModel.completeTask()
		}

		viewModel.onCompleteTaskError.observe(viewLifecycleOwner) {
			showSnackBarResult(it)
		}
	}

	@SuppressLint("ShowToast")
	private fun showSnackBarResult(id: String) {
		Snackbar.make(
			requireView(),
			"Completing $id failed",
			Snackbar.LENGTH_SHORT
		).setAnchorView(binding.completeTask).show()
	}
}