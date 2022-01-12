package com.cabbagebeyond.ui.admin.roles

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.cabbagebeyond.databinding.FragmentRoleManagementListItemBinding
import com.cabbagebeyond.model.Role


class RoleManagementAdapter(private val clickListener: RoleClickListener) :
    ListAdapter<Role, RoleManagementAdapter.ViewHolder>(
        DiffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder(private val binding: FragmentRoleManagementListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: Role, clickListener: RoleClickListener) {
            binding.role = user
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FragmentRoleManagementListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Role>() {
        override fun areItemsTheSame(oldItem: Role, newItem: Role): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Role, newItem: Role): Boolean {
            return oldItem.id == newItem.id
        }
    }

}

class RoleClickListener(val clickListener: (role: Role) -> Unit) {
    fun onClick(role: Role) = clickListener(role)
}