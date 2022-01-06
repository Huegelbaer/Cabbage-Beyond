package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.UserDataSource
import com.cabbagebeyond.data.dao.UserDao
import com.cabbagebeyond.data.dto.asDatabaseModel
import com.cabbagebeyond.data.dto.asDomainModel
import com.cabbagebeyond.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val userDao: UserDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserDataSource {

    override suspend fun getUsers(): Result<List<User>> = withContext(ioDispatcher) {
        return@withContext userDao.getUsers().mapCatching { it.asDomainModel() }
    }

    override suspend fun getUser(id: String): Result<User> = withContext(ioDispatcher) {
        return@withContext userDao.getUser(id).mapCatching { it.asDomainModel() }
    }

    override suspend fun saveUser(user: User) = withContext(ioDispatcher) {
        userDao.saveUser(user.asDatabaseModel())
    }

    override suspend fun deleteUser(id: String) = withContext(ioDispatcher) {
        userDao.deleteUser(id)
    }
}