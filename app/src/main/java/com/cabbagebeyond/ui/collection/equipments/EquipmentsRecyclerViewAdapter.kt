package com.cabbagebeyond.ui.collection.equipments

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.cabbagebeyond.databinding.FragmentEquipmentsListItemBinding
import com.cabbagebeyond.model.Equipment
import com.cabbagebeyond.ui.CollectionDiffCallback
import com.cabbagebeyond.ui.CollectionItemClickListener

class EquipmentsRecyclerViewAdapter(private val clickListener: EquipmentClickListener) :
    ListAdapter<Equipment, EquipmentsRecyclerViewAdapter.ViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder(private val binding: FragmentEquipmentsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Equipment, clickListener: EquipmentClickListener) {
            binding.equipment = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    FragmentEquipmentsListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    companion object DiffCallback : CollectionDiffCallback<Equipment>()
}

class EquipmentClickListener(clickListener: (item: Equipment) -> Unit) :
    CollectionItemClickListener<Equipment>(clickListener)