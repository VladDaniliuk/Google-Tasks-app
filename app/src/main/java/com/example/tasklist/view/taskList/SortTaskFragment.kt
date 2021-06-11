package com.example.tasklist.view.taskList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tasklist.databinding.FragmentSortTaskBinding
import com.example.tasklist.viewModel.taskList.SortTaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SortTaskFragment : BottomSheetDialogFragment() {
	private val viewModel: SortTaskViewModel by viewModels()
	private lateinit var binding: FragmentSortTaskBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentSortTaskBinding.inflate(inflater,container,false)
		binding.viewModel = viewModel
		binding.lifecycleOwner = viewLifecycleOwner
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		viewModel.onCompletedTask.observe(viewLifecycleOwner) {
			findNavController().previousBackStackEntry?.savedStateHandle?.set("key", "Complete")
			findNavController().popBackStack()
		}

		viewModel.onDeletedTask.observe(viewLifecycleOwner) {
			findNavController().previousBackStackEntry?.savedStateHandle?.set("key", "Complete")
			findNavController().popBackStack()
		}

		viewModel.onSortTask.observe(viewLifecycleOwner) {
			findNavController().previousBackStackEntry?.savedStateHandle?.set("key", "Complete")
			findNavController().popBackStack()
		}
	}
}