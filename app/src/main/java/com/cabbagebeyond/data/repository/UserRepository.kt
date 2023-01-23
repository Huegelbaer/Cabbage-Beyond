package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.UserDataSource
import com.cabbagebeyond.data.remote.dto.UserDTO
import com.cabbagebeyond.data.local.dao.UserDao
import com.cabbagebeyond.data.local.entities.UserEntity
import com.cabbagebeyond.data.local.relations.UserRoleCrossRef
import com.cabbagebeyond.data.local.relations.UserWithRoles
import com.cabbagebeyond.data.remote.service.UserService
import com.cabbagebeyond.model.Role
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
        val result = userDao.getUsers()
        val list = result.map { it.asDomainModel() }
        return@withContext Result.success(list)
    }

    override suspend fun getUser(id: String): Result<User> = withContext(ioDispatcher) {
        val result = userDao.getUser(id)
        val user = result.asDomainModel()
        return@withContext Result.success(user)
    }

    override suspend fun getUserByEmail(email: String): Result<User?> = withContext(ioDispatcher) {
        val result = userDao.getUserByEmail(email)
        val list = result?.asDomainModel()
        return@withContext Result.success(list)
    }

    override suspend fun saveUser(user: User) = withContext(ioDispatcher) {
        userDao.saveUser(user.asDatabaseModel())
    }

    override suspend fun deleteUser(user: User) = withContext(ioDispatcher) {
        userDao.deleteUser(user.asDatabaseModel())
    }

    override suspend fun refreshUsers() = withContext(ioDispatcher) {
        val result = userService.refreshUsers()
        if (result.isSuccess) {
            result.getOrNull()?.forEach {
                save(it)
            }
        }
    }

    override suspend fun refreshUser(id: String) = withContext(ioDispatcher) {
        val result = userService.refreshUser(id)
        if (result.isSuccess) {
            result.getOrNull()?.let {
                save(it)
            }
        }
    }

    private suspend fun save(user: UserDTO) {
        val roles = user.roles_ids.map {
            UserRoleCrossRef(user.id, it)
        }
        userDao.saveRoles(roles)
        userDao.saveUser(user.asDatabaseModel())
    }
}

private fun UserDTO.asDatabaseModel(): UserEntity {
    return UserEntity(username, email, features, roles_ids, id)
}

private fun UserWithRoles.asDomainModel(): User {
    val roles = roles.map { it.asDomainModel() }
    return user.asDomainModel(roles)
}

fun UserEntity.asDomainModel(roles: List<Role>): User {
    return User(username, email, features, roles, id)
}

private fun User.asDatabaseModel(): UserEntity {
    return UserEntity(name, email, features, roles.map { it.id }, id)
}