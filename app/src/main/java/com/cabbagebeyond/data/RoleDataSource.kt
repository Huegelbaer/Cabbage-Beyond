package com.cabbagebeyond.data

import com.cabbagebeyond.model.Role

interface RoleDataSource {
    
    //fun observeRoles(): LiveData<Result<List<Role>>>

    suspend fun getRoles(): Result<List<Role>>

    //suspend fun refreshRoles()

    //fun observeRole(id: String): LiveData<Result<Role>>

    suspend fun getRole(id: String): Result<Role>

    //suspend fun refreshRole(id: String)

    suspend fun saveRole(role: Role)

    //suspend fun deleteAllRoles()

    suspend fun deleteRole(id: String)
}