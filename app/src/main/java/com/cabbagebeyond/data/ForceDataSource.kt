package com.cabbagebeyond.data

import com.cabbagebeyond.model.Force

interface ForceDataSource {

    //fun observeForces(): LiveData<Result<List<Force>>>

    suspend fun getForces(): Result<List<Force>>

    //suspend fun refreshForces()

    //fun observeForce(id: String): LiveData<Result<Force>>

    suspend fun getForce(id: String): Result<Force>

    //suspend fun refreshForce(id: String)

    suspend fun saveForce(force: Force)

    //suspend fun deleteAllForces()

    suspend fun deleteForce(id: String)
}