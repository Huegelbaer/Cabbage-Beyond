package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.RaceDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.data.remote.dto.RaceDTO
import com.cabbagebeyond.data.local.dao.RaceDao
import com.cabbagebeyond.data.local.entities.RaceEntity
import com.cabbagebeyond.data.local.entities.RaceFeatureEntity
import com.cabbagebeyond.data.local.entities.WorldEntity
import com.cabbagebeyond.data.local.relations.RaceFeatureCrossRef
import com.cabbagebeyond.data.local.relations.RaceWithWorld
import com.cabbagebeyond.data.remote.service.RaceService
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
                save(it)
            }
        }
    }

    override suspend fun refreshRace(id: String) = withContext(ioDispatcher) {
        val result = raceService.refreshRace(id)
        if (result.isSuccess) {
            result.getOrNull()?.let {
                save(it)
            }
        }
    }

    private suspend fun save(raceDTO: RaceDTO) {
        val race = raceDTO.asDatabaseModel()
        val features = raceDTO.raceFeatures.map { it.asDatabaseModel() }
        raceDao.saveRace(race)
        raceDao.saveFeatures(features)
        raceDao.saveRaceFeatures(features.map { RaceFeatureCrossRef(race.id, it.id) })
    }
}

private fun RaceDTO.asDatabaseModel(): RaceEntity {
    return RaceEntity(name, description, world, id)
}

private fun RaceDTO.Feature.asDatabaseModel(): RaceFeatureEntity {
    return RaceFeatureEntity(description, name, id)
}

private fun Race.asDatabaseModel(): RaceEntity {
    return RaceEntity(name, description, world?.id ?: "", id)
}

private fun List<RaceWithWorld>.asDomainModel(): List<Race> {
    return map {
        it.asDomainModel()
    }
}

private fun RaceWithWorld.asDomainModel(): Race {
    return race.asDomainModel(world)
}

fun RaceEntity.asDomainModel(worldEntity: WorldEntity?): Race {
    return Race(
        name,
        description,
        listOf(),
        worldEntity?.asDomainModel(),
        id
    )
}

private fun RaceFeatureEntity.asDomainModel(): Race.Feature {
    return Race.Feature(description, name, id)
}