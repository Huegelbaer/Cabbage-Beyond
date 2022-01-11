package com.cabbagebeyond.ui.admin.roles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.data.dao.RoleDao
import com.cabbagebeyond.data.repository.RoleRepository
import com.cabbagebeyond.model.Role
import kotlinx.coroutines.launch

class RoleManagementViewModel: ViewModel() {
    private val _roles = MutableLiveData<List<Role>>()
    val roles: LiveData<List<Role>>
        get() = _roles

    private lateinit var roleRepository: RoleRepository

    init {

        roleRepository = RoleRepository(RoleDao())

        viewModelScope.launch {
            fetchRoles()
        }
    }

    private suspend fun fetchRoles() {
        _roles.value = roleRepository.getRoles().getOrDefault(listOf())
    }

    fun change(feature: List<String>, role: Role, name: String) {
        role.name = name
        role.features = feature
        save(role)
    }

    private fun save(role: Role) {
        viewModelScope.launch {
            roleRepository.saveRole(role)
            reloadRoles()
        }
    }

    fun delete(role: Role) {
        viewModelScope.launch {
            roleRepository.deleteRole(role.id)
            reloadRoles()
        }
    }

    private suspend fun reloadRoles() {
        _roles.value = roleRepository.getRoles().getOrDefault(listOf())
    }
}