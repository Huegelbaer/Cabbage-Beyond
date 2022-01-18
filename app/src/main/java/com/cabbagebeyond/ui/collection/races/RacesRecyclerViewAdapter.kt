package com.cabbagebeyond.ui.collection.races

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cabbagebeyond.databinding.FragmentRacesListItemBinding
import com.cabbagebeyond.model.Race
import com.cabbagebeyond.ui.CollectionDiffCallback
import com.cabbagebeyond.ui.CollectionItemClickListener

class RacesRecyclerViewAdapter(private val clickListener: RaceClickListener) :
    ListAdapter<Race, RacesRecyclerViewAdapter.ViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder(private val binding: FragmentRacesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Race, clickListener: RaceClickListener) {
            binding.race = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    FragmentRacesListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    companion object DiffCallback : CollectionDiffCallback<Race>()
}

class RaceClickListener(clickListener: (item: Race) -> Unit) :
    CollectionItemClickListener<Race>(clickListener)