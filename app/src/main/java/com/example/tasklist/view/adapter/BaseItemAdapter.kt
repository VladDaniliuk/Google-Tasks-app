package com.example.tasklist.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.view.itemModel.BaseItemModel

class BaseItemAdapter<T : BaseItemModel, B : ViewDataBinding>(
	private val variableId: Int,
	@LayoutRes
	private val layoutRes: Int
) : ListAdapter<T, BaseItemAdapter<T, B>.ViewHolder>(UserItemDiffCallback()) {
	override fun onViewAttachedToWindow(holder: ViewHolder) {
		super.onViewAttachedToWindow(holder)
		holder.onStart()
	}

	inner class ViewHolder(private var binding: ViewDataBinding) :
		LifecycleViewHolder(binding.root) {
		var baseItem: BaseItemModel? = null

		fun bind(item: T, variableId: Int) {
			binding.lifecycleOwner = this
			binding.setVariable(variableId, item)
			binding.executePendingBindings()
			ViewCompat.setTransitionName(binding.root, item.id)
			baseItem = item
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = DataBindingUtil.inflate<B>(inflater, layoutRes, parent, false)
		val holder = ViewHolder(binding)
		holder.onCreate()
		return holder
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(getItem(position), variableId)
	}

	fun onItemMove(fromPosition: Int, toPosition: Int) {
		val list = currentList.toMutableList()
		val movedItem = list[fromPosition]
		with(list) {
			removeAt(fromPosition)
			add(toPosition, movedItem)
		}
		submitList(list)
	}
}

class UserItemDiffCallback<V : BaseItemModel> : DiffUtil.ItemCallback<V>() {
	override fun areItemsTheSame(oldItem: V, newItem: V): Boolean {
		return oldItem.id == newItem.id
	}

	@SuppressLint("DiffUtilEquals")
	override fun areContentsTheSame(oldItem: V, newItem: V): Boolean {
		return oldItem == newItem
	}
}

abstract class LifecycleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
	LifecycleOwner {
	private val lifecycleRegistry = LifecycleRegistry(this)

	init {
		lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
	}

	fun onCreate() {
		lifecycleRegistry.currentState = Lifecycle.State.CREATED
	}

	fun onStart() {
		lifecycleRegistry.currentState = Lifecycle.State.STARTED
	}

	override fun getLifecycle(): Lifecycle {
		return lifecycleRegistry
	}
}