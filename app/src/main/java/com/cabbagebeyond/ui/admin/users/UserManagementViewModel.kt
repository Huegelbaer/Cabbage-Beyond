package com.cabbagebeyond.ui.admin.users

import androidx.lifecycle.*
import com.cabbagebeyond.data.dao.RoleDao
import com.cabbagebeyond.data.dao.UserDao
import com.cabbagebeyond.data.repository.RoleRepository
import com.cabbagebeyond.data.repository.UserRepository
import com.cabbagebeyond.model.Role
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.util.FirebaseUtil
import kotlinx.coroutines.launch

class UserManagementViewModel: ViewModel() {

    data class Data(var user: User, var roles: List<Role>)

    private val _users = MutableLiveData<List<User>>()
    private val _roles = MutableLiveData<List<Role>>()
    val roles: LiveData<List<Role>>
        get() = _roles

    private val _md = MediatorLiveData<List<Data>>()
    val users: LiveData<List<Data>>
        get() = _md
  //  val users = MediatorLiveData<List<Data>>()

    private lateinit var userRepository: UserRepository
    private lateinit var roleRepository: RoleRepository

    init {

        userRepository = UserRepository(UserDao())
        roleRepository = RoleRepository(RoleDao())

        viewModelScope.launch {
            fetchUsersAndRoles()
        }
    }

    private suspend fun fetchUsersAndRoles() {
        val firstValue = userRepository.getUsers()
        _users.value = firstValue.getOrDefault(listOf())

        val secondValue = roleRepository.getRoles()
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

}