package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.RaceDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.data.dao.RaceDao
import com.cabbagebeyond.data.dto.RaceDTO
import com.cabbagebeyond.model.Race
import com.cabbagebeyond.model.World
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RaceRepository(
    private val raceDao: RaceDao,
    private val worldDataSource: WorldDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RaceDataSource {

    override suspend fun getRaces(): Result<List<Race>> = withContext(ioDispatcher) {
        val result = raceDao.getRaces()
        return@withContext mapList(result)
    }

    override suspend fun getRace(id: String): Result<Race> = withContext(ioDispatcher) {
        val result = raceDao.getRace(id)
        return@withContext map(result)
    }

    override suspend fun saveRace(race: Race): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext raceDao.saveRace(race.asDatabaseModel())
    }

    override suspend fun deleteRace(id: String): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext raceDao.deleteRace(id)
    }

    private suspend fun mapList(result: Result<List<RaceDTO>>): Result<List<Race>> {
        val worlds = worldDataSource.getWorlds().getOrDefault(listOf())
        return result.mapCatching {
            it.asDomainModel(worlds)
        }
    }

    private suspend fun map(result: Result<RaceDTO>): Result<Race> {
        return result.mapCatching {
            val world = worldDataSource.getWorld(it.world).getOrNull()
            it.asDomainModel(world)
        }
    }
}


fun List<RaceDTO>.asDomainModel(worlds: List<World>): List<Race> {
    return map { race ->
        race.asDomainModel(worlds.firstOrNull { it.id == race.world })
    }
}

fun RaceDTO.asDomainModel(world: World?): Race {
    return Race(name, description, raceFeatures, world, id)
}

fun List<Race>.asDatabaseModel(): List<RaceDTO> {
    return map {
        it.asDatabaseModel()
    }
}

fun Race.asDatabaseModel(): RaceDTO {
    return RaceDTO(name, description, raceFeatures, world?.id ?: "", id)
}