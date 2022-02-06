package com.cabbagebeyond.ui.admin.users

import androidx.lifecycle.*
import com.cabbagebeyond.data.RoleDataSource
import com.cabbagebeyond.data.UserDataSource
import com.cabbagebeyond.model.Role
import com.cabbagebeyond.model.User
import kotlinx.coroutines.launch

class UserManagementViewModel(
    private val userDataSource: UserDataSource,
    private val roleDataSource: RoleDataSource
) : ViewModel() {

    data class Data(var user: User, var roles: List<Role>)

    private val _users = MutableLiveData<List<User>>()
    private val _roles = MutableLiveData<List<Role>>()
    val roles: LiveData<List<Role>>
        get() = _roles

    private val _md = MediatorLiveData<List<Data>>()
    val users: LiveData<List<Data>>
        get() = _md
    //  val users = MediatorLiveData<List<Data>>()

    init {

        viewModelScope.launch {
            fetchUsersAndRoles()
        }
    }

    private suspend fun fetchUsersAndRoles() {
        val firstValue = userDataSource.getUsers()
        _users.value = firstValue.getOrDefault(listOf())

        val secondValue = roleDataSource.getRoles()
        _roles.value = secondValue.getOrDefault(listOf())

        var isFirstEmitted = false
        var isSecondEmitted = false

        _md.addSource(_users) { users ->
            isFirstEmitted = true
            if (isFirstEmitted && isSecondEmitted) {
                createData(users, _roles.value!!)
            }
        }
        _md.addSource(_roles) { roles ->
            isSecondEmitted = true
            if (isFirstEmitted && isSecondEmitted) {
                createData(_users.value!!, roles)
            }
        }
    }

    private fun createData(users: List<User>, roles: List<Role>) {
        _md.value = users.map {
            val role = roles.filter { role ->
                it.roles.contains(role.id)
            }
            Data(it, role)
        }
    }

    fun change(roles: List<Role>, data: Data) {
        val user = data.user
        user.roles = roles.map { it.id }
        save(user)
    }

    private fun save(user: User) {
        viewModelScope.launch {
            userDataSource.saveUser(user)
            reloadUsers()
        }
    }

    fun delete(user: Data) {
        viewModelScope.launch {
            userDataSource.deleteUser(user.user.id)
            reloadUsers()
        }
        _users.value = _users.value?.filterNot { it.id == user.user.id }
    }

    private suspend fun reloadUsers() {
        _users.value = userDataSource.getUsers().getOrDefault(listOf())
    }
}