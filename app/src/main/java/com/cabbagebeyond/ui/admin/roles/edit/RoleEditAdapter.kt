package com.cabbagebeyond.ui.admin.roles.edit

import com.cabbagebeyond.databinding.FragmentRoleEditFeatureBinding

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.cabbagebeyond.util.Feature


class RoleEditAdapter(private val clickListener: FeatureClickListener) :
    ListAdapter<Feature, RoleEditAdapter.ViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder(private val binding: FragmentRoleEditFeatureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(title: Feature, clickListener: FeatureClickListener) {
            binding.feature = title
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FragmentRoleEditFeatureBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Feature>() {
        override fun areItemsTheSame(oldItem: Feature, newItem: Feature): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Feature, newItem: Feature): Boolean {
            return oldItem.name == newItem.name
        }
    }
}

class FeatureClickListener(val clickListener: (title: Feature) -> Unit) {
    fun onClick(title: Feature) = clickListener(title)
}