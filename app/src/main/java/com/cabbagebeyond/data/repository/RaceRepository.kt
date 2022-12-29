package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.RaceDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.data.dto.RaceDTO
import com.cabbagebeyond.data.local.dao.RaceDao
import com.cabbagebeyond.data.local.entities.RaceEntity
import com.cabbagebeyond.data.local.entities.asDomainModel
import com.cabbagebeyond.data.local.relations.RaceWithWorld
import com.cabbagebeyond.data.remote.RaceService
import com.cabbagebeyond.model.Race
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RaceRepository(
    private val raceDao: RaceDao,
    private val raceService: RaceService,
    private val worldDataSource: WorldDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RaceDataSource {

    override suspend fun getRaces(): Result<List<Race>> = withContext(ioDispatcher) {
        val result = raceDao.getRaces()
        val list = result.map { it.asDomainModel() }
        return@withContext Result.success(list)
    }

    override suspend fun getRace(id: String): Result<Race> = withContext(ioDispatcher) {
        val result = raceDao.getRace(id)
        return@withContext Result.success(result.asDomainModel())
    }

    override suspend fun saveRace(race: Race): Result<Boolean> = withContext(ioDispatcher) {
        raceDao.saveRace(race.asDatabaseModel())
        return@withContext Result.success(true)
    }

    override suspend fun deleteRace(race: Race): Result<Boolean> = withContext(ioDispatcher) {
        raceDao.deleteRace(race.asDatabaseModel())
        return@withContext Result.success(true)
    }

    override suspend fun refreshRaces() = withContext(ioDispatcher) {
        val result = raceService.refreshRaces()
        if (result.isSuccess) {
            result.getOrNull()?.forEach {
                raceDao.saveRace(it.asDatabaseModel())
            }
        }
    }

    override suspend fun refreshRace(id: String) = withContext(ioDispatcher) {
        val result = raceService.refreshRace(id)
        if (result.isSuccess) {
            result.getOrNull()?.let {
                raceDao.saveRace(it.asDatabaseModel())
            }
        }
    }
}

fun RaceDTO.asDatabaseModel(): RaceEntity {
    return RaceEntity(name, description, raceFeatures, world, id)
}

fun List<Race>.asDatabaseModel(): List<RaceEntity> {
    return map {
        it.asDatabaseModel()
    }
}

fun Race.asDatabaseModel(): RaceEntity {
    return RaceEntity(name, description, raceFeatures, world?.id ?: "", id)
}

fun List<RaceWithWorld>.asDomainModel(): List<Race> {
    return map {
        it.asDomainModel()
    }
}

fun RaceWithWorld.asDomainModel(): Race {
    return Race(race.name, race.description, race.features, world?.asDomainModel(), race.id)
}