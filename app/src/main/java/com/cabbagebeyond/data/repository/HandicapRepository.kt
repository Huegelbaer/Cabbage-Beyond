package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.HandicapDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.data.local.dao.HandicapDao
import com.cabbagebeyond.data.dto.HandicapDTO
import com.cabbagebeyond.data.remote.HandicapService
import com.cabbagebeyond.model.Handicap
import com.cabbagebeyond.model.World
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HandicapRepository(
    private val handicapDao: HandicapDao,
    private val handicapService: HandicapService,
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

    override suspend fun refreshHandicaps(): Result<Boolean> = withContext(ioDispatcher) {
        handicapService.refreshHandicaps()
    }

    override suspend fun refreshHandicap(id: String): Result<Boolean> = withContext(ioDispatcher) {
        handicapService.refreshHandicap(id)
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
    return Handicap(name, description, valueToHandicapType(type), world, id)
}

fun List<Handicap>.asDatabaseModel(): List<HandicapDTO> {
    return map {
        it.asDatabaseModel()
    }
}

fun Handicap.asDatabaseModel(): HandicapDTO {
    return HandicapDTO(name, description, type?.asDatabaseModel() ?: "", world?.id ?: "", id)
}


fun valueToHandicapType(dtoValue: String?): Handicap.Type? {
    return when(dtoValue) {
        "Leicht" -> Handicap.Type.SLIGHT
        "Leicht/Schwer" -> Handicap.Type.SLIGHT_OR_HEAVY
        "Schwer" -> Handicap.Type.HEAVY
        else -> null
    }
}

fun Handicap.Type.asDatabaseModel(): String {
    return when(this) {
        Handicap.Type.SLIGHT -> "Leicht"
        Handicap.Type.SLIGHT_OR_HEAVY -> "Leicht/Schwer"
        Handicap.Type.HEAVY -> "Schwer"
    }
}