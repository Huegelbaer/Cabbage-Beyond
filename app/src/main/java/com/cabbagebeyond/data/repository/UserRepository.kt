package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.UserDataSource
import com.cabbagebeyond.data.local.dao.UserDao
import com.cabbagebeyond.data.dto.asDatabaseModel
import com.cabbagebeyond.data.dto.asDomainModel
import com.cabbagebeyond.data.remote.UserService
import com.cabbagebeyond.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val userDao: UserDao,
    private val userService: UserService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserDataSource {

    override suspend fun getUsers(): Result<List<User>> = withContext(ioDispatcher) {
        return@withContext userDao.getUsers().mapCatching { it.asDomainModel() }
    }

    override suspend fun getUser(id: String): Result<User> = withContext(ioDispatcher) {
        return@withContext userDao.getUser(id).mapCatching { it.asDomainModel() }
    }

    override suspend fun getUserByEmail(email: String): Result<User> = withContext(ioDispatcher) {
        return@withContext userDao.getUserByEmail(email).mapCatching { it.asDomainModel() }
    }

    override suspend fun saveUser(user: User) = withContext(ioDispatcher) {
        userDao.saveUser(user.asDatabaseModel())
    }

    override suspend fun deleteUser(id: String) = withContext(ioDispatcher) {
        userDao.deleteUser(id)
    }

    override suspend fun refreshUsers(): Result<Boolean> = withContext(ioDispatcher) {
        userService.refreshUsers()
    }

    override suspend fun refreshUser(id: String): Result<Boolean> = withContext(ioDispatcher) {
        userService.refreshUser(id)
    }
}