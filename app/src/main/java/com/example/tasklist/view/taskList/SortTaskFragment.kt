package com.example.tasklist.view.taskList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tasklist.R
import com.example.tasklist.databinding.FragmentSortTaskBinding
import com.example.tasklist.view.base.BaseBottomSheetDialogFragment
import com.example.tasklist.viewModel.taskList.SortTaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SortTaskFragment(override val viewModelClass: Class<SortTaskViewModel>) :
	BaseBottomSheetDialogFragment<FragmentSortTaskBinding, SortTaskViewModel>() {
	private val args: SortTaskFragmentArgs by navArgs()

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
			viewModel.setting.value?.let { setting ->
				binding.completedTask.text = getString(
					R.string.completed_tasks,
					when (setting.first) {
						getString(R.string.show) -> getString(R.string.hide)
						else -> getString(R.string.show)
					}
				)
				binding.deletedTasks.text = getString(
					R.string.show__tasks,
					when (setting.third) {
						getString(R.string.deleted_small) -> getString(R.string.assigned)
						else -> getString(R.string.deleted_small)
					}
				)
			}
		}

		viewModel.onRadioButtonChoose.observe(viewLifecycleOwner) { radioButtonId ->
			viewModel.setting.value?.let { setting ->
				viewModel.postSetting.postValue(
					setting.copy(
						second = binding.sortMethodes.checkedRadioButtonId,
						third = when (radioButtonId) {
							R.id.my_order -> getString(R.string.assigned)
							else -> setting.third
						}
					)
				)
			}
		}
	}
}