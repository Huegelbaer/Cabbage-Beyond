package com.cabbagebeyond.data

import com.cabbagebeyond.model.Force

interface ForceDataSource {

    suspend fun getForces(): Result<List<Force>>

    suspend fun getForces(ids: List<String>): Result<List<Force>>

    suspend fun getForce(id: String): Result<Force>

    suspend fun saveForce(force: Force): Result<Boolean>

    suspend fun deleteForce(force: Force): Result<Boolean>

    /*
        REMOTE
    */

    suspend fun refreshForces()

    suspend fun refreshForce(id: String)
}