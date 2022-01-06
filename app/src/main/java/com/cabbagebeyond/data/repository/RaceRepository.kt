package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.RaceDataSource
import com.cabbagebeyond.data.dao.RaceDao
import com.cabbagebeyond.data.dto.asDatabaseModel
import com.cabbagebeyond.data.dto.asDomainModel
import com.cabbagebeyond.model.Race
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RaceRepository(
    private val raceDao: RaceDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RaceDataSource {

    override suspend fun getRaces(): Result<List<Race>> = withContext(ioDispatcher) {
        return@withContext raceDao.getRaces().mapCatching { it.asDomainModel() }
    }

    override suspend fun getRace(id: String): Result<Race> = withContext(ioDispatcher) {
        return@withContext raceDao.getRace(id).mapCatching { it.asDomainModel() }
    }

    override suspend fun saveRace(race: Race) = withContext(ioDispatcher) {
        raceDao.saveRace(race.asDatabaseModel())
    }

    override suspend fun deleteRace(id: String) = withContext(ioDispatcher) {
        raceDao.deleteRace(id)
    }
}