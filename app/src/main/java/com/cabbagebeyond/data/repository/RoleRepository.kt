package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.RoleDataSource
import com.cabbagebeyond.data.dto.RoleDTO
import com.cabbagebeyond.data.dao.RoleDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoleRepository(
    private val roleDao: RoleDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RoleDataSource {

    override suspend fun getRoles(): Result<List<RoleDTO>> = withContext(ioDispatcher) {
        return@withContext roleDao.getRoles()
    }

    override suspend fun getRole(id: String): Result<RoleDTO> = withContext(ioDispatcher) {
        return@withContext roleDao.getRole(id)
    }

    override suspend fun saveRole(role: RoleDTO) = withContext(ioDispatcher) {
        roleDao.saveRole(role)
    }

    override suspend fun deleteRole(id: String) = withContext(ioDispatcher) {
        roleDao.deleteRole(id)
    }
}