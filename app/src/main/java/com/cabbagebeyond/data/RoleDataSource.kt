package com.cabbagebeyond.data

import com.cabbagebeyond.model.Role

interface RoleDataSource {
    
    //fun observeRoles(): LiveData<Result<List<Role>>>

    //fun observeRole(id: String): LiveData<Result<Role>>

    suspend fun getRoles(): Result<List<Role>>

    suspend fun getRole(id: String): Result<Role>

    suspend fun saveRole(role: Role)

    suspend fun deleteRole(id: String)

    /*
        REMOTE
     */

    suspend fun refreshRoles(): Result<Boolean>

    suspend fun refreshRole(id: String): Result<Boolean>
}