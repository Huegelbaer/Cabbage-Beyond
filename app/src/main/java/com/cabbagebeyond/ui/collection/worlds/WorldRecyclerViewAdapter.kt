package com.cabbagebeyond.ui.collection.worlds

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.cabbagebeyond.databinding.FragmentWorldsListItemBinding
import com.cabbagebeyond.model.World


class WorldRecyclerViewAdapter : ListAdapter<World, WorldRecyclerViewAdapter.WorldViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: WorldViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorldViewHolder {
        return WorldViewHolder.from(parent)
    }

    class WorldViewHolder(private val binding: FragmentWorldsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(world: World) {
            binding.world = world
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): WorldViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FragmentWorldsListItemBinding.inflate(layoutInflater, parent, false)
                return WorldViewHolder(binding)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<World>() {
        override fun areItemsTheSame(oldItem: World, newItem: World): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: World, newItem: World): Boolean {
            return oldItem.id == newItem.id
        }
    }
}