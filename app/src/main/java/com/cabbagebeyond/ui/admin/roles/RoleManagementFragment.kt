package com.cabbagebeyond.ui.admin.roles

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cabbagebeyond.R
import com.cabbagebeyond.data.AbilityDataSource
import com.cabbagebeyond.data.RoleDataSource
import com.cabbagebeyond.databinding.FragmentRoleManagementListBinding
import com.cabbagebeyond.model.Role
import com.cabbagebeyond.ui.admin.roles.edit.RoleEditDialogFragment
import com.cabbagebeyond.ui.collection.abilities.AbilitiesViewModel
import org.koin.android.ext.android.inject

class RoleManagementFragment : Fragment() {

    private val _viewModel: RoleManagementViewModel  by lazy {
        val dataSource: RoleDataSource by inject()
        RoleManagementViewModel(dataSource)
    }

    private lateinit var _binding: FragmentRoleManagementListBinding
    private lateinit var _adapter: RoleManagementAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoleManagementListBinding.inflate(inflater)

        val clickListener = RoleClickListener {
            editRole(it)
        }
        _adapter = RoleManagementAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }

        _viewModel.roles.observe(viewLifecycleOwner, Observer { roles ->
            roles?.let {
                _adapter.submitList(roles)
            }
        })

        _binding.addRole.setOnClickListener {
            val role = Role("", listOf())
            editRole(role)
        }

        return _binding.root
    }

    private fun editRole(role: Role) {
        val dialog = RoleEditDialogFragment.newInstance(role.name, role.features)
        dialog.onSave = { name, selectedFeatures ->
            _viewModel.change(selectedFeatures, role, name)
        }
        dialog.onDelete = {
            showConfirmDialog(role.name) { _, _ ->
                _viewModel.delete(role)
            }
        }

        dialog.show(childFragmentManager, RoleEditDialogFragment.TAG)
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