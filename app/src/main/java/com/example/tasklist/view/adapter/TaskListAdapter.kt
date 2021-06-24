package com.example.tasklist.view.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.example.tasklist.view.itemModel.TaskListItemModel


class TaskListAdapter(
	context: Context,
	@LayoutRes private val layoutResource: Int,
	private val taskLists: List<TaskListItemModel>
) : ArrayAdapter<TaskListItemModel>(context, layoutResource, taskLists) {

	var selectedItem: TaskListItemModel? = null

	override fun getPosition(item: TaskListItemModel?): Int {
		return taskLists.indexOf(item)
	}

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		val view = super.getView(position, convertView, parent)
		val textView = view.findViewById<TextView>(android.R.id.text1)
		textView.text = getItem(position).title
		return view
	}

	override fun getItem(position: Int): TaskListItemModel {
		return taskLists[position]
	}

	fun getTaskListItem(parentId: String): TaskListItemModel? {
		return taskLists.find {
			it.id == parentId
		}
	}

	fun setItemSelected(position: Int) {
		selectedItem = taskLists[position]
	}
}