package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.HandicapDataSource
import com.cabbagebeyond.data.dao.HandicapDao
import com.cabbagebeyond.data.dto.asDatabaseModel
import com.cabbagebeyond.data.dto.asDomainModel
import com.cabbagebeyond.model.Handicap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HandicapRepository(
    private val handicapDao: HandicapDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : HandicapDataSource {

    override suspend fun getHandicaps(): Result<List<Handicap>> = withContext(ioDispatcher) {
        return@withContext handicapDao.getHandicaps().mapCatching { it.asDomainModel() }
    }

    override suspend fun getHandicap(id: String): Result<Handicap> = withContext(ioDispatcher) {
        return@withContext handicapDao.getHandicap(id).mapCatching { it.asDomainModel() }
    }

    override suspend fun saveHandicap(handicap: Handicap) = withContext(ioDispatcher) {
        handicapDao.saveHandicap(handicap.asDatabaseModel())
    }

    override suspend fun deleteHandicap(id: String) = withContext(ioDispatcher) {
        handicapDao.deleteHandicap(id)
    }
}