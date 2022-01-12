package com.cabbagebeyond.ui.admin

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cabbagebeyond.ui.admin.roles.RoleManagementFragment
import com.cabbagebeyond.ui.admin.users.UserManagementFragment

class AdminPanelAdapter(fragment: Fragment, private val itemsCount: Int): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 1) {
            RoleManagementFragment()
        } else {
            UserManagementFragment()
        }
    }
}