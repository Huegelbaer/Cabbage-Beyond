package com.cabbagebeyond.ui.collection.characters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.cabbagebeyond.databinding.FragmentCharacterListItemBinding
import com.cabbagebeyond.model.Character
import com.cabbagebeyond.ui.CollectionDiffCallback
import com.cabbagebeyond.ui.CollectionItemClickListener

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

    companion object DiffCallback : CollectionDiffCallback<Character>()
}

class CharacterClickListener(clickListener: (item: Character) -> Unit): CollectionItemClickListener<Character>(clickListener)