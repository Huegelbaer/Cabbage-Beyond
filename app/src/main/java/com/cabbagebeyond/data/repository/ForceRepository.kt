package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.ForceDataSource
import com.cabbagebeyond.data.dto.ForceDTO
import com.cabbagebeyond.data.dao.ForceDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ForceRepository(
    private val forceDao: ForceDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ForceDataSource {

    override suspend fun getForces(): Result<List<ForceDTO>> = withContext(ioDispatcher) {
        return@withContext forceDao.getForces()
    }

    override suspend fun getForce(id: String): Result<ForceDTO> = withContext(ioDispatcher) {
        return@withContext forceDao.getForce(id)
    }

    override suspend fun saveForce(force: ForceDTO) = withContext(ioDispatcher) {
        return@withContext forceDao.saveForce(force)
    }

    override suspend fun deleteForce(id: String) = withContext(ioDispatcher) {
        return@withContext forceDao.deleteForce(id)
    }
}