package com.cabbagebeyond.data

import com.cabbagebeyond.data.dto.RoleDTO

interface RoleDataSource {
    
    //fun observeRoles(): LiveData<Result<List<RoleDTO>>>

    suspend fun getRoles(): Result<List<RoleDTO>>

    //suspend fun refreshRoles()

    //fun observeRole(id: String): LiveData<Result<RoleDTO>>

    suspend fun getRole(id: String): Result<RoleDTO>

    //suspend fun refreshRole(id: String)

    suspend fun saveRole(role: RoleDTO)

    //suspend fun deleteAllRoles()

    suspend fun deleteRole(id: String)
}