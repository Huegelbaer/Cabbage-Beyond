package com.cabbagebeyond.ui.collection.characters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.cabbagebeyond.databinding.FragmentCharacterListItemBinding
import com.cabbagebeyond.model.Character

class CharacterListAdapter(private val clickListener: CharacterClickListener) :
    ListAdapter<Character, CharacterListAdapter.ViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder(private val binding: FragmentCharacterListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Character, clickListener: CharacterClickListener) {
            binding.character = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FragmentCharacterListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }
}

class CharacterClickListener(val clickListener: (item: Character) -> Unit) {
    fun onClick(item: Character) = clickListener(item)
}