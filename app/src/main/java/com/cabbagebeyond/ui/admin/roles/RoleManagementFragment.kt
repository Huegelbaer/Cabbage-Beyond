package com.cabbagebeyond.ui.admin.roles

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cabbagebeyond.R
import com.cabbagebeyond.model.Role
import com.cabbagebeyond.ui.admin.users.UserClickListener
import com.cabbagebeyond.ui.admin.users.UserManagementViewModel

/**
 * A fragment representing a list of Items.
 */
class RoleManagementFragment : Fragment() {

    private val _viewModel: RoleManagementViewModel by activityViewModels()
    private lateinit var _adapter: RoleManagementAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_role_management_list, container, false)

        val clickListener = RoleClickListener {
            editRole(it)
        }
        _adapter = RoleManagementAdapter(clickListener)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = _adapter
            }
        }

        _viewModel.roles.observe(viewLifecycleOwner, Observer { roles ->
            roles?.let {
                _adapter.submitList(roles)
            }
        })

        return view
    }

    private fun editRole(role: Role) {
        /*val selectedRoles = user.roles.toMutableList()
        val roles = _viewModel.roles.value ?: listOf()
        val titles = roles.map { it.name }
        val checked = roles.map { user.roles.contains(it) }.toBooleanArray()
*/
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_title_edit_user)
            .setMessage(role.name)
                /*
            .setMultiChoiceItems(titles.toTypedArray(), checked) { _, which, isChecked ->
                val item = roles[which]
                if (isChecked) {
                    selectedRoles.add(item)
                } else if (selectedRoles.contains(item)) {
                    selectedRoles.remove(item)
                }
            }.setPositiveButton(R.string.dialog_button_save) { _, _ ->
                _viewModel.change(selectedRoles, user)
            }*/
            .setNeutralButton(R.string.dialog_button_cancel) { _, _ -> }
       /*     .setNegativeButton(R.string.dialog_button_delete) { _, _ ->
                showConfirmDialog(user.user.email) { _, _ ->
                    _viewModel.delete(user)
                }
            }*/
            .create()
            .show()
    }

    private fun showConfirmDialog(name: String, listener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_title_attention)
            .setMessage(getString(R.string.dialog_message_delete_user, name))
            .setPositiveButton(R.string.dialog_button_delete, listener)
            .setNeutralButton(R.string.dialog_button_cancel) { _, _ -> }
            .create()
            .show()
    }
}