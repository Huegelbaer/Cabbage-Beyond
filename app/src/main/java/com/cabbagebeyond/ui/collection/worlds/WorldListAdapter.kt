package com.cabbagebeyond.ui.collection.worlds

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.cabbagebeyond.databinding.FragmentWorldListItemBinding
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.CollectionDiffCallback
import com.cabbagebeyond.ui.CollectionItemClickListener


class WorldListAdapter(private val clickListener: WorldClickListener) :
    ListAdapter<World, WorldListAdapter.WorldViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: WorldViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorldViewHolder {
        return WorldViewHolder.from(parent)
    }

    class WorldViewHolder(private val binding: FragmentWorldListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(world: World, clickListener: WorldClickListener) {
            binding.world = world
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): WorldViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FragmentWorldListItemBinding.inflate(layoutInflater, parent, false)
                return WorldViewHolder(binding)
            }
        }
    }

    companion object DiffCallback: CollectionDiffCallback<World>()
}

class WorldClickListener(clickListener: (item: World) -> Unit): CollectionItemClickListener<World>(clickListener)