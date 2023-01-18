package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.RoleDataSource
import com.cabbagebeyond.data.dto.RoleDTO
import com.cabbagebeyond.data.local.dao.RoleDao
import com.cabbagebeyond.data.local.entities.RoleEntity
import com.cabbagebeyond.data.remote.RoleService
import com.cabbagebeyond.model.Role
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoleRepository(
    private val roleDao: RoleDao,
    private val roleService: RoleService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RoleDataSource {

    override suspend fun getRoles(): Result<List<Role>> = withContext(ioDispatcher) {
        val result = roleDao.getRoles()
        val list = result.map { it.asDomainModel() }
        return@withContext Result.success(list)
    }

    override suspend fun getRole(id: String): Result<Role> = withContext(ioDispatcher) {
        val result = roleDao.getRole(id)
        val role = result.asDomainModel()
        return@withContext Result.success(role)
    }

    override suspend fun saveRole(role: Role) = withContext(ioDispatcher) {
        roleDao.saveRole(role.asDatabaseModel())
    }

    override suspend fun deleteRole(role: Role) = withContext(ioDispatcher) {
        roleDao.deleteRole(role.asDatabaseModel())
    }

    override suspend fun refreshRoles() = withContext(ioDispatcher) {
        val result = roleService.refreshRoles()
        if (result.isSuccess) {
            result.getOrNull()?.forEach {
                saveRole(it.asDomainModel())
            }
        }
    }

    override suspend fun refreshRole(id: String) = withContext(ioDispatcher) {
        val result = roleService.refreshRole(id)
        if (result.isSuccess) {
            result.getOrNull()?.let {
                saveRole(it.asDomainModel())
            }
        }
    }
}

fun List<RoleDTO>.asDomainModel(): List<Role> {
    return map {
        it.asDomainModel()
    }
}

fun RoleDTO.asDomainModel(): Role {
    return Role(name, features, id)
}

fun RoleEntity.asDomainModel(): Role {
    return Role(name, features, id)
}

fun List<Role>.asDatabaseModel(): List<RoleEntity> {
    return map {
        it.asDatabaseModel()
    }
}

fun Role.asDatabaseModel(): RoleEntity {
    return RoleEntity(name, features, id)
}