package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.HandicapDataSource
import com.cabbagebeyond.data.dto.HandicapDTO
import com.cabbagebeyond.data.dao.HandicapDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HandicapRepository(
    private val handicapDao: HandicapDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : HandicapDataSource {

    override suspend fun getHandicaps(): Result<List<HandicapDTO>> = withContext(ioDispatcher) {
        return@withContext handicapDao.getHandicaps()
    }

    override suspend fun getHandicap(id: String): Result<HandicapDTO> = withContext(ioDispatcher) {
        return@withContext handicapDao.getHandicap(id)
    }

    override suspend fun saveHandicap(handicap: HandicapDTO) = withContext(ioDispatcher) {
        handicapDao.saveHandicap(handicap)
    }

    override suspend fun deleteHandicap(id: String) = withContext(ioDispatcher) {
        handicapDao.deleteHandicap(id)
    }
}