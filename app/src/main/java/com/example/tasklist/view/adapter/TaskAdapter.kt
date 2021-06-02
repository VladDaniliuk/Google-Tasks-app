package com.example.tasklist.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.R
import com.example.tasklist.databinding.LayoutAdditionalInfoBinding
import com.example.tasklist.databinding.LayoutDueDateBinding
import com.example.tasklist.databinding.LayoutSubtasksBinding
import com.example.tasklist.dev.SingleLiveEvent
import com.example.tasklist.extensions.DebounceTextWatcher
import com.example.tasklist.extensions.bindingIsDue
import com.example.tasklist.view.itemModel.TaskItemModel

class TaskAdapter(
	private val onChipClick: View.OnClickListener,
	private val onChipClose: View.OnClickListener,
	private val onItem: SingleLiveEvent<String>
) :
	RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
	var taskItemModel: TaskItemModel? = null
		set(value) {
			field = value
			notifyDataSetChanged()
		}

	inner class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = when (viewType) {
			0 -> {
				DataBindingUtil.inflate<LayoutAdditionalInfoBinding>(
					inflater,
					R.layout.layout_additional_info,
					parent,
					false
				)
			}
			1 -> {
				DataBindingUtil.inflate<LayoutDueDateBinding>(
					inflater,
					R.layout.layout_due_date,
					parent,
					false
				)
			}
			else -> {
				DataBindingUtil.inflate<LayoutSubtasksBinding>(
					inflater,
					R.layout.layout_subtasks,
					parent,
					false
				)
			}
		}
		return ViewHolder(binding)
	}

	override fun getItemCount(): Int = taskItemModel?.let { 3 } ?: 0

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		when (position) {
			0 -> {
				val binding = holder.binding as LayoutAdditionalInfoBinding
				binding.textInputEditText2.setText(taskItemModel?.notes)
				binding.textInputEditText2.addTextChangedListener(DebounceTextWatcher(onItem))
			}
			1 -> {
				val binding = holder.binding as LayoutDueDateBinding
				binding.chip4.bindingIsDue(taskItemModel?.dueDate)
				binding.textView.bindingIsDue(taskItemModel?.dueDate)
				binding.chip4.setOnCloseIconClickListener(onChipClose)
				binding.onClick = onChipClick
			}
			else -> {
			}
		}
	}

	override fun getItemViewType(position: Int): Int {
		return position
	}
}