package com.cabbagebeyond.data

import com.cabbagebeyond.model.Role

interface RoleDataSource {

    suspend fun getRoles(): Result<List<Role>>

    suspend fun getRole(id: String): Result<Role>

    suspend fun saveRole(role: Role)

    suspend fun deleteRole(role: Role)

    /*
        REMOTE
     */

    suspend fun refreshRoles()

    suspend fun refreshRole(id: String)
}