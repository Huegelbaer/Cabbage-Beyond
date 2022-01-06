package com.cabbagebeyond.ui.admin.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.data.dao.UserDao
import com.cabbagebeyond.data.repository.UserRepository
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import kotlinx.coroutines.launch

class UserManagementViewModel: ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    private lateinit var repository: UserRepository

    init {
        repository = UserRepository(UserDao())

        viewModelScope.launch {
            val result = repository.getUsers()
            _users.value = result.getOrNull() ?: listOf()
        }
    }

}