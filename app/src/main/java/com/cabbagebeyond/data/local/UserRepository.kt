package com.cabbagebeyond.data.local

import com.cabbagebeyond.data.UserDataSource
import com.cabbagebeyond.data.dto.UserDTO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val userDao: UserDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserDataSource {

    override suspend fun getUsers(): Result<List<UserDTO>> = withContext(ioDispatcher) {
        return@withContext userDao.getUsers()
    }

    override suspend fun getUser(id: String): Result<UserDTO> = withContext(ioDispatcher) {
        return@withContext userDao.getUser(id)
    }

    override suspend fun saveUser(user: UserDTO) = withContext(ioDispatcher) {
        userDao.saveUser(user)
    }

    override suspend fun deleteUser(id: String) = withContext(ioDispatcher) {
        userDao.deleteUser(id)
    }
}