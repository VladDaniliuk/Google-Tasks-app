package com.example.tasklist.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.view.itemModel.BaseItemModel

class ListAdapter<T : BaseItemModel, B : ViewDataBinding>(
	private val variableId: Int,
	@LayoutRes
	private val layoutRes: Int,
	private val items: List<T>
) : RecyclerView.Adapter<ListAdapter<T, B>.ViewHolder>() {
	inner class ViewHolder(private var binding: ViewDataBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun bind(item: T, variableId: Int) {
			binding.setVariable(variableId, item)
			binding.executePendingBindings()
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter<T, B>.ViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = DataBindingUtil.inflate<B>(inflater, layoutRes, parent, false)
		return ViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ListAdapter<T, B>.ViewHolder, position: Int) {
		holder.bind(items[position], variableId)
	}

	override fun getItemCount(): Int = items.size
}