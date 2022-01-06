package com.cabbagebeyond.ui.admin.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.cabbagebeyond.R

/**
 * A fragment representing a list of Items.
 */
class UserManagementFragment : Fragment() {

    private val _viewModel: UserManagementViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_managment_list, container, false)

        val clickListener = UserClickListener {}
        val usersAdapter = UserManagementAdapter(clickListener)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = usersAdapter
            }
        }

        _viewModel.users.observe(viewLifecycleOwner, Observer { users ->
            users?.let {
                usersAdapter.submitList(it)
            }
        })

        return view
    }
}