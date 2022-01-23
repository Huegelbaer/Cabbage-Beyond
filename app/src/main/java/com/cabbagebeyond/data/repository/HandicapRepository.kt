package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.HandicapDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.data.dao.HandicapDao
import com.cabbagebeyond.data.dto.EquipmentDTO
import com.cabbagebeyond.data.dto.HandicapDTO
import com.cabbagebeyond.data.dto.asDatabaseModel
import com.cabbagebeyond.data.dto.asDomainModel
import com.cabbagebeyond.model.Equipment
import com.cabbagebeyond.model.Handicap
import com.cabbagebeyond.model.World
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HandicapRepository(
    private val handicapDao: HandicapDao,
    private val worldDataSource: WorldDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : HandicapDataSource {

    override suspend fun getHandicaps(): Result<List<Handicap>> = withContext(ioDispatcher) {
        val result = handicapDao.getHandicaps()
        return@withContext mapList(result)
    }

    override suspend fun getHandicaps(ids: List<String>): Result<List<Handicap>> = withContext(ioDispatcher) {
        val result = handicapDao.getHandicaps(ids)
        return@withContext mapList(result)
    }

    override suspend fun getHandicap(id: String): Result<Handicap> = withContext(ioDispatcher) {
        val result = handicapDao.getHandicap(id)
        return@withContext map(result)
    }

    override suspend fun saveHandicap(handicap: Handicap): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext handicapDao.saveHandicap(handicap.asDatabaseModel())
    }

    override suspend fun deleteHandicap(id: String): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext handicapDao.deleteHandicap(id)
    }

    private suspend fun mapList(result: Result<List<HandicapDTO>>): Result<List<Handicap>> {
        val worlds = worldDataSource.getWorlds().getOrDefault(listOf())
        return result.mapCatching {
            it.asDomainModel(worlds)
        }
    }

    private suspend fun map(result: Result<HandicapDTO>): Result<Handicap> {
        return result.mapCatching {
            val world = worldDataSource.getWorld(it.world).getOrNull()
            it.asDomainModel(world)
        }
    }
}

fun List<HandicapDTO>.asDomainModel(worlds: List<World>): List<Handicap> {
    return map { handicap ->
        handicap.asDomainModel(worlds.firstOrNull { it.id == handicap.world })
    }
}

fun HandicapDTO.asDomainModel(world: World?): Handicap {
    return Handicap(name, description, type, world, id)
}

fun List<Handicap>.asDatabaseModel(): List<HandicapDTO> {
    return map {
        it.asDatabaseModel()
    }
}

fun Handicap.asDatabaseModel(): HandicapDTO {
    return HandicapDTO(name, description, type, world?.id ?: "", id)
}