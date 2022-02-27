package com.cabbagebeyond.ui.admin.users

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cabbagebeyond.R
import com.cabbagebeyond.data.RoleDataSource
import com.cabbagebeyond.data.UserDataSource
import com.cabbagebeyond.databinding.FragmentUserManagmentListBinding
import org.koin.android.ext.android.inject

class UserManagementFragment : Fragment() {

    private val _viewModel: UserManagementViewModel by lazy {
        val userDataSource: UserDataSource by inject()
        val roleDataSource: RoleDataSource by inject()
        UserManagementViewModel(userDataSource, roleDataSource)
    }

    private lateinit var _binding: FragmentUserManagmentListBinding
    private lateinit var _adapter: UserManagementAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserManagmentListBinding.inflate(inflater)

        val clickListener = UserClickListener {
            editUser(it)
        }
        _adapter = UserManagementAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }

        _viewModel.users.observe(viewLifecycleOwner) { users ->
            users?.let {
                _adapter.submitList(it)
            }
        }

        return _binding.root
    }

    private fun editUser(user: UserManagementViewModel.Data) {
        val selectedRoles = user.roles.toMutableList()
        val roles = _viewModel.roles.value ?: listOf()
        val titles = roles.map { it.name }
        val checked = roles.map { user.roles.contains(it) }.toBooleanArray()

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_title_edit_user)
            .setMultiChoiceItems(titles.toTypedArray(), checked) { _, which, isChecked ->
                val item = roles[which]
                if (isChecked) {
                    selectedRoles.add(item)
                } else if (selectedRoles.contains(item)) {
                    selectedRoles.remove(item)
                }
            }.setPositiveButton(R.string.dialog_button_save) { _, _ ->
                _viewModel.change(selectedRoles, user)
            }
            .setNeutralButton(R.string.dialog_button_cancel) { _, _ -> }
            .setNegativeButton(R.string.dialog_button_delete) { _, _ ->
                showConfirmDialog(user.user.email) { _, _ ->
                    _viewModel.delete(user)
                }
            }
            .create()
            .show()
    }

    private fun showConfirmDialog(username: String, listener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_title_attention)
            .setMessage(getString(R.string.dialog_message_delete_user, username))
            .setPositiveButton(R.string.dialog_button_delete, listener)
            .setNeutralButton(R.string.dialog_button_cancel) { _, _ -> }
            .create()
            .show()
    }
}