package com.example.tasklist.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.R
import com.example.tasklist.databinding.LayoutTaskAddSubtaskBinding
import com.example.tasklist.view.itemModel.TaskItemModel

class AddSubTaskAdapter(private val onClick: View.OnClickListener) :
	RecyclerView.Adapter<AddSubTaskAdapter.ViewHolder>() {
	var taskItemModel: TaskItemModel? = null
		set(value) {
			field = value
			notifyDataSetChanged()
		}

	inner class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = DataBindingUtil.inflate<LayoutTaskAddSubtaskBinding>(
			inflater,
			R.layout.layout_task_add_subtask,
			parent,
			false
		)
		return ViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		(holder.binding as LayoutTaskAddSubtaskBinding).onClick = onClick
	}

	override fun getItemCount(): Int = taskItemModel?.let { 1 } ?: 0
}