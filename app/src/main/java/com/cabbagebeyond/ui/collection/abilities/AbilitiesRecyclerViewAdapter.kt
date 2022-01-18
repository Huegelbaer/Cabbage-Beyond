package com.cabbagebeyond.ui.collection.abilities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cabbagebeyond.databinding.FragmentAbilitiesListItemBinding
import com.cabbagebeyond.model.Ability
import com.cabbagebeyond.ui.CollectionDiffCallback
import com.cabbagebeyond.ui.CollectionItemClickListener
import com.cabbagebeyond.ui.collection.abilities.placeholder.PlaceholderContent.PlaceholderItem

class AbilitiesRecyclerViewAdapter(private val clickListener: AbilityClickListener) :
    ListAdapter<Ability, AbilitiesRecyclerViewAdapter.ViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder(private val binding: FragmentAbilitiesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Ability, clickListener: AbilityClickListener) {
            binding.ability = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    FragmentAbilitiesListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    companion object DiffCallback : CollectionDiffCallback<Ability>()
}

class AbilityClickListener(clickListener: (item: Ability) -> Unit) :
    CollectionItemClickListener<Ability>(clickListener)