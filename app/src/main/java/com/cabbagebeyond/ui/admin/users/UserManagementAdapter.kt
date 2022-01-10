package com.cabbagebeyond.ui.admin.users

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.cabbagebeyond.databinding.FragmentUserManagmentListItemBinding

class UserManagementAdapter(private val clickListener: UserClickListener) :
    ListAdapter<UserManagementViewModel.Data, UserManagementAdapter.UserViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.from(parent)
    }

    class UserViewHolder(private val binding: FragmentUserManagmentListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserManagementViewModel.Data, clickListener: UserClickListener) {
            binding.data = user
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): UserViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FragmentUserManagmentListItemBinding.inflate(layoutInflater, parent, false)
                return UserViewHolder(binding)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<UserManagementViewModel.Data>() {
        override fun areItemsTheSame(oldItem: UserManagementViewModel.Data, newItem: UserManagementViewModel.Data): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: UserManagementViewModel.Data, newItem: UserManagementViewModel.Data): Boolean {
            return oldItem.user.id == newItem.user.id
        }
    }
}

class UserClickListener(val clickListener: (user: UserManagementViewModel.Data) -> Unit) {
    fun onClick(user: UserManagementViewModel.Data) = clickListener(user)
}