package com.cabbagebeyond.ui.admin

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdminPanelAdapter(fragment: Fragment, private val itemsCount: Int): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        /*return if (position == 1) {
            RoleManagmentFragment()
        } else {
            UserManagmentFragment()
        }*/
        return Fragment()
    }
}