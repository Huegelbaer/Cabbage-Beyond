package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.ForceDataSource
import com.cabbagebeyond.data.dao.ForceDao
import com.cabbagebeyond.data.dto.asDatabaseModel
import com.cabbagebeyond.data.dto.asDomainModel
import com.cabbagebeyond.model.Force
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ForceRepository(
    private val forceDao: ForceDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ForceDataSource {

    override suspend fun getForces(): Result<List<Force>> = withContext(ioDispatcher) {
        return@withContext forceDao.getForces().mapCatching { it.asDomainModel() }
    }

    override suspend fun getForces(ids: List<String>): Result<List<Force>> = withContext(ioDispatcher) {
        return@withContext forceDao.getForces(ids).mapCatching { it.asDomainModel() }
    }

    override suspend fun getForce(id: String): Result<Force> = withContext(ioDispatcher) {
        return@withContext forceDao.getForce(id).mapCatching { it.asDomainModel() }
    }

    override suspend fun saveForce(force: Force) = withContext(ioDispatcher) {
        return@withContext forceDao.saveForce(force.asDatabaseModel())
    }

    override suspend fun deleteForce(id: String) = withContext(ioDispatcher) {
        return@withContext forceDao.deleteForce(id)
    }
}