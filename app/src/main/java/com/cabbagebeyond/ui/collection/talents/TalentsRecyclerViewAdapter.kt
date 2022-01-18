package com.cabbagebeyond.ui.collection.talents

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.cabbagebeyond.databinding.FragmentTalentsListItemBinding
import com.cabbagebeyond.model.Talent
import com.cabbagebeyond.ui.CollectionDiffCallback
import com.cabbagebeyond.ui.CollectionItemClickListener

class TalentsRecyclerViewAdapter(private val clickListener: TalentClickListener) :
    ListAdapter<Talent, TalentsRecyclerViewAdapter.ViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder(private val binding: FragmentTalentsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Talent, clickListener: TalentClickListener) {
            binding.talent = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    FragmentTalentsListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    companion object DiffCallback : CollectionDiffCallback<Talent>()
}

class TalentClickListener(clickListener: (item: Talent) -> Unit) :
    CollectionItemClickListener<Talent>(clickListener)