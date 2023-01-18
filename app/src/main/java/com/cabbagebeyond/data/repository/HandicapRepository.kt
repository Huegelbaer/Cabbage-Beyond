package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.HandicapDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.data.dto.HandicapDTO
import com.cabbagebeyond.data.local.dao.HandicapDao
import com.cabbagebeyond.data.local.entities.HandicapEntity
import com.cabbagebeyond.data.local.entities.WorldEntity
import com.cabbagebeyond.data.local.relations.HandicapWithWorld
import com.cabbagebeyond.data.remote.HandicapService
import com.cabbagebeyond.model.Handicap
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
        val list = result.map { it.asDomainModel() }
        return@withContext Result.success(list)
    }

    override suspend fun getHandicaps(ids: List<String>): Result<List<Handicap>> =
        withContext(ioDispatcher) {
            val result = handicapDao.getHandicaps(ids)
            val list = result.map { it.asDomainModel() }
            return@withContext Result.success(list)
        }

    override suspend fun getHandicap(id: String): Result<Handicap> = withContext(ioDispatcher) {
        val result = handicapDao.getHandicap(id)
        return@withContext Result.success(result.asDomainModel())
    }

    override suspend fun saveHandicap(handicap: Handicap): Result<Boolean> =
        withContext(ioDispatcher) {
            handicapDao.saveHandicap(handicap.asDatabaseModel())
            return@withContext Result.success(true)
        }

    override suspend fun deleteHandicap(handicap: Handicap): Result<Boolean> =
        withContext(ioDispatcher) {
            handicapDao.deleteHandicap(handicap.asDatabaseModel())
            return@withContext Result.success(true)
        }

    override suspend fun refreshHandicaps() = withContext(ioDispatcher) {
        val result = handicapService.refreshHandicaps()
        if (result.isSuccess) {
            result.getOrNull()?.forEach {
                handicapDao.saveHandicap(it.asDatabaseModel())
            }
        }
    }

    override suspend fun refreshHandicap(id: String) = withContext(ioDispatcher) {
        val result = handicapService.refreshHandicap(id)
        if (result.isSuccess) {
            result.getOrNull()?.let {
                handicapDao.saveHandicap(it.asDatabaseModel())
            }
        }
    }
}

private fun List<HandicapWithWorld>.asDomainModel(): List<Handicap> {
    return map { handicap ->
        handicap.asDomainModel()
    }
}

private fun HandicapWithWorld.asDomainModel(): Handicap {
    return handicap.asDomainModel(world)
}

fun HandicapEntity.asDomainModel(worldEntity: WorldEntity?): Handicap {
    return Handicap(
        name,
        description,
        type.asDomainModel(),
        worldEntity?.asDomainModel(),
        id
    )
}

private fun List<Handicap>.asDatabaseModel(): List<HandicapEntity> {
    return map {
        it.asDatabaseModel()
    }
}

private fun Handicap.asDatabaseModel(): HandicapEntity {
    return HandicapEntity(name, description, type.asDatabaseModel(), world?.id ?: "", id)
}

private fun HandicapDTO.asDatabaseModel(): HandicapEntity {
    return HandicapEntity(name, description, valueToHandicapType(type), world, id)
}

private fun valueToHandicapType(dtoValue: String): HandicapEntity.Type {
    return when (dtoValue) {
        "Leicht" -> HandicapEntity.Type.SLIGHT
        "Leicht/Schwer" -> HandicapEntity.Type.SLIGHT_OR_HEAVY
        "Schwer" -> HandicapEntity.Type.HEAVY
        else -> HandicapEntity.Type.SLIGHT_OR_HEAVY
    }
}

private fun Handicap.Type.asDatabaseModel(): HandicapEntity.Type {
    return when (this) {
        Handicap.Type.SLIGHT -> HandicapEntity.Type.SLIGHT
        Handicap.Type.SLIGHT_OR_HEAVY -> HandicapEntity.Type.SLIGHT_OR_HEAVY
        Handicap.Type.HEAVY -> HandicapEntity.Type.HEAVY
    }
}

private fun HandicapEntity.Type.asDomainModel(): Handicap.Type {
    return when (this) {
        HandicapEntity.Type.SLIGHT -> Handicap.Type.SLIGHT
        HandicapEntity.Type.SLIGHT_OR_HEAVY -> Handicap.Type.SLIGHT_OR_HEAVY
        HandicapEntity.Type.HEAVY -> Handicap.Type.HEAVY
    }
}