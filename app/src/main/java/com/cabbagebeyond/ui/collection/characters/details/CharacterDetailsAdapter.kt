package com.cabbagebeyond.ui.collection.characters.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cabbagebeyond.databinding.FragmentCharacterDetailsListHeaderItemBinding
import com.cabbagebeyond.databinding.FragmentCharacterDetailsListItemBinding

class CharacterDetailsAdapter(
    private val expandHeader: ((headerItem: HeaderItem) -> Unit),
    private val collapseHeader: ((headerItem: HeaderItem) -> Unit),
    private val showItem: ((listItem: ListItem) -> Unit),
    private val removeItem: ((listItem: ListItem) -> Unit)
) : ListAdapter<Item, RecyclerView.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> HeaderViewHolder.from(parent)
            1 -> ListItemViewHolder.from(parent)
            else -> ListItemViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is HeaderItem -> (holder as HeaderViewHolder).bind(item, {
                expandHeader(it)
            }, {
                collapseHeader(it)
            })
            is ListItem -> (holder as ListItemViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item::class.java) {
            CharacterDetailsViewModel.HeaderItem::class.java -> 0
            CharacterDetailsViewModel.ListItem::class.java -> 1
            else -> 2
        }
    }

    class HeaderViewHolder(
        private val binding: FragmentCharacterDetailsListHeaderItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: HeaderItem,
            checkedAction: ((item: HeaderItem) -> Unit),
            uncheckedAction: ((item: HeaderItem) -> Unit)
        ) {
            binding.item = item
            binding.executePendingBindings()

            binding.toggle.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    checkedAction(item)
                } else {
                    uncheckedAction(item)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FragmentCharacterDetailsListHeaderItemBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return HeaderViewHolder(binding)
            }
        }
    }

    class ListItemViewHolder(private val binding: FragmentCharacterDetailsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ListItem) {
            binding.item = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ListItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    FragmentCharacterDetailsListItemBinding.inflate(layoutInflater, parent, false)
                return ListItemViewHolder(binding)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.title == newItem.title
        }
    }
}

typealias Item = CharacterDetailsViewModel.Item
typealias HeaderItem = CharacterDetailsViewModel.HeaderItem
typealias ListItem = CharacterDetailsViewModel.ListItem