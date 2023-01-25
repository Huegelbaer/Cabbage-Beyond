package com.cabbagebeyond.ui.collection.forces

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cabbagebeyond.databinding.FragmentForceListItemBinding
import com.cabbagebeyond.model.Force
import com.cabbagebeyond.ui.CollectionDiffCallback
import com.cabbagebeyond.ui.CollectionItemClickListener


class ForceListAdapter(private val clickListener: ForceClickListener) :
    ListAdapter<Force, ForceListAdapter.ViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder(private val binding: FragmentForceListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Force, clickListener: ForceClickListener) {
            binding.force = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    FragmentForceListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    companion object DiffCallback : CollectionDiffCallback<Force>()
}

class ForceClickListener(clickListener: (item: Force) -> Unit) :
    CollectionItemClickListener<Force>(clickListener)