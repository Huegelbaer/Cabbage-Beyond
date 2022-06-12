package com.cabbagebeyond.ui.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.cabbagebeyond.R

class AdminPanelFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin_panel, container, false)

        val adapter = AdminPanelAdapter(this, 2)
        view.findViewById<ViewPager2>(R.id.admin_panel_pager).adapter = adapter

        return view
    }
}