package com.cabbagebeyond.ui.admin.roles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.data.RoleDataSource
import com.cabbagebeyond.model.Role
import kotlinx.coroutines.launch

class RoleManagementViewModel(private val roleDataSource: RoleDataSource): ViewModel() {

    private val _roles = MutableLiveData<List<Role>>()
    val roles: LiveData<List<Role>>
        get() = _roles

    init {
        viewModelScope.launch {
            fetchRoles()
        }
    }

    private suspend fun fetchRoles() {
        _roles.value = roleDataSource.getRoles().getOrDefault(listOf())
    }

    fun change(feature: List<String>, role: Role, name: String) {
        role.name = name
        role.features = feature
        save(role)
    }

    private fun save(role: Role) {
        viewModelScope.launch {
            roleDataSource.saveRole(role)
            reloadRoles()
        }
    }

    fun delete(role: Role) {
        viewModelScope.launch {
            roleDataSource.deleteRole(role)
            reloadRoles()
        }
    }

    private suspend fun reloadRoles() {
        _roles.value = roleDataSource.getRoles().getOrDefault(listOf())
    }
}