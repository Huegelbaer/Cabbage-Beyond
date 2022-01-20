package com.cabbagebeyond.ui.collection.handicaps

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.cabbagebeyond.databinding.FragmentHandicapsListItemBinding
import com.cabbagebeyond.model.Handicap
import com.cabbagebeyond.ui.CollectionDiffCallback
import com.cabbagebeyond.ui.CollectionItemClickListener

class HandicapsRecyclerViewAdapter(private val clickListener: HandicapClickListener) :
    ListAdapter<Handicap, HandicapsRecyclerViewAdapter.ViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder(private val binding: FragmentHandicapsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Handicap, clickListener: HandicapClickListener) {
            binding.handicap = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    FragmentHandicapsListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    companion object DiffCallback : CollectionDiffCallback<Handicap>()
}

class HandicapClickListener(clickListener: (item: Handicap) -> Unit) :
    CollectionItemClickListener<Handicap>(clickListener)