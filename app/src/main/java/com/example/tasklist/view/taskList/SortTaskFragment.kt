package com.example.tasklist.view.taskList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tasklist.R
import com.example.tasklist.databinding.FragmentSortTaskBinding
import com.example.tasklist.viewModel.taskList.SortTaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SortTaskFragment : BottomSheetDialogFragment() {
	private val args: SortTaskFragmentArgs by navArgs()
	private lateinit var binding: FragmentSortTaskBinding
	private val viewModel: SortTaskViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel.setting.postValue(Triple(args.completedTasks, args.sortByDate, args.deletedTasks))
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentSortTaskBinding.inflate(inflater, container, false)
		binding.viewModel = viewModel
		binding.lifecycleOwner = viewLifecycleOwner

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		viewModel.postSetting.observe(viewLifecycleOwner) { setting ->
			findNavController().previousBackStackEntry?.savedStateHandle?.set(
				"key",
				setting
			)
			findNavController().popBackStack()
		}

		viewModel.setting.observe(viewLifecycleOwner) {
			binding.completedTask.text = getString(
				R.string.completed_tasks,
				if (viewModel.setting.value?.first == getString(R.string.show))
					getString(R.string.hide)
				else
					getString(R.string.show)
			)
			binding.sortTasks.text = getString(
				R.string.sort_by_date_to,
				if (viewModel.setting.value?.second == getString(R.string.add))
					getString(R.string.complete)
				else
					getString(R.string.add)
			)
			binding.deletedTasks.text = getString(
				R.string.show__tasks,
				if (viewModel.setting.value?.third == getString(R.string.deleted_small))
					getString(R.string.assigned)
				else getString(R.string.deleted_small)
			)
		}

		viewModel.onDeletedTask.observe(viewLifecycleOwner) {
			findNavController().popBackStack()
		}
	}
}