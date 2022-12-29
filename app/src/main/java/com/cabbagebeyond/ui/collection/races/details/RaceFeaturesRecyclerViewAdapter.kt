package com.cabbagebeyond.ui.collection.races.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cabbagebeyond.databinding.FragmentRaceDetailsFeatureListItemBinding
import com.cabbagebeyond.model.Race
import com.cabbagebeyond.ui.CollectionDiffCallback


class RaceFeaturesRecyclerViewAdapter() :
    ListAdapter<Race.Feature, RaceFeaturesRecyclerViewAdapter.ViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder(private val binding: FragmentRaceDetailsFeatureListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Race.Feature) {
            binding.item = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    FragmentRaceDetailsFeatureListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    companion object DiffCallback : CollectionDiffCallback<Race.Feature>()
}