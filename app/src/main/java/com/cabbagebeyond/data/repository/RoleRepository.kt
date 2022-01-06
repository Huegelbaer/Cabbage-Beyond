package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.RoleDataSource
import com.cabbagebeyond.data.dao.RoleDao
import com.cabbagebeyond.data.dto.asDatabaseModel
import com.cabbagebeyond.data.dto.asDomainModel
import com.cabbagebeyond.model.Role
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoleRepository(
    private val roleDao: RoleDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RoleDataSource {

    override suspend fun getRoles(): Result<List<Role>> = withContext(ioDispatcher) {
        return@withContext roleDao.getRoles().mapCatching { it.asDomainModel() }
    }

    override suspend fun getRole(id: String): Result<Role> = withContext(ioDispatcher) {
        return@withContext roleDao.getRole(id).mapCatching { it.asDomainModel() }
    }

    override suspend fun saveRole(role: Role) = withContext(ioDispatcher) {
        roleDao.saveRole(role.asDatabaseModel())
    }

    override suspend fun deleteRole(id: String) = withContext(ioDispatcher) {
        roleDao.deleteRole(id)
    }
}