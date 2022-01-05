package com.cabbagebeyond.data

import com.cabbagebeyond.data.dto.ForceDTO

interface ForceDataSource {

    //fun observeForces(): LiveData<Result<List<ForceDTO>>>

    suspend fun getForces(): Result<List<ForceDTO>>

    //suspend fun refreshForces()

    //fun observeForce(id: String): LiveData<Result<ForceDTO>>

    suspend fun getForce(id: String): Result<ForceDTO>

    //suspend fun refreshForce(id: String)

    suspend fun saveForce(force: ForceDTO)

    //suspend fun deleteAllForces()

    suspend fun deleteForce(id: String)
}