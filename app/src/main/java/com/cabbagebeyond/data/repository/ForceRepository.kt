package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.ForceDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.data.dto.ForceDTO
import com.cabbagebeyond.data.local.asDatabaseModel
import com.cabbagebeyond.data.local.asDomainModel
import com.cabbagebeyond.data.local.dao.ForceDao
import com.cabbagebeyond.data.local.entities.ForceEntity
import com.cabbagebeyond.data.local.entities.WorldEntity
import com.cabbagebeyond.data.local.entities.asDomainModel
import com.cabbagebeyond.data.local.relations.ForceWithWorld
import com.cabbagebeyond.data.local.valueToRank
import com.cabbagebeyond.data.remote.ForceService
import com.cabbagebeyond.model.Force
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ForceRepository(
    private val forceDao: ForceDao,
    private val forceService: ForceService,
    private val worldDataSource: WorldDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ForceDataSource {

    override suspend fun getForces(): Result<List<Force>> = withContext(ioDispatcher) {
        val result = forceDao.getForces()
        val list = result.map { it.asDomainModel() }
        return@withContext Result.success(list)
    }

    override suspend fun getForces(ids: List<String>): Result<List<Force>> =
        withContext(ioDispatcher) {
            val result = forceDao.getForces(ids)
            val list = result.map { it.asDomainModel() }
            return@withContext Result.success(list)
        }

    override suspend fun getForce(id: String): Result<Force> = withContext(ioDispatcher) {
        val result = forceDao.getForce(id)
        return@withContext Result.success(result.asDomainModel())
    }

    override suspend fun saveForce(force: Force): Result<Boolean> = withContext(ioDispatcher) {
        forceDao.saveForce(force.asDatabaseModel())
        return@withContext Result.success(true)
    }

    override suspend fun deleteForce(force: Force): Result<Boolean> = withContext(ioDispatcher) {
        forceDao.deleteForce(force.asDatabaseModel())
        return@withContext Result.success(true)
    }

    override suspend fun refreshForces() = withContext(ioDispatcher) {
        val result = forceService.refreshForces()
        if (result.isSuccess) {
            result.getOrNull()?.forEach {
                forceDao.saveForce(it.asDatabaseModel())
            }
        }
    }

    override suspend fun refreshForce(id: String) = withContext(ioDispatcher) {
        val result = forceService.refreshForce(id)
        if (result.isSuccess) {
            result.getOrNull()?.let {
                forceDao.saveForce(it.asDatabaseModel())
            }
        }
    }

}


fun List<ForceWithWorld>.asDomainModel(): List<Force> {
    return map { force ->
        force.asDomainModel()
    }
}

fun ForceWithWorld.asDomainModel(): Force {
    return force.asDomainModel(world)
}

fun ForceEntity.asDomainModel(worldEntity: WorldEntity?): Force {
    return Force(
        name,
        description,
        cost,
        duration,
        requiredRank.asDomainModel(),
        range,
        shaping,
        worldEntity?.asDomainModel(),
        id
    )
}

fun List<Force>.asDatabaseModel(): List<ForceEntity> {
    return map {
        it.asDatabaseModel()
    }
}

fun Force.asDatabaseModel(): ForceEntity {
    return ForceEntity(
        name,
        description,
        cost,
        duration,
        rangRequirement.asDatabaseModel(),
        range,
        shaping,
        world?.id ?: "",
        id
    )
}

fun ForceDTO.asDatabaseModel(): ForceEntity {
    return ForceEntity(
        name,
        description,
        cost,
        duration,
        valueToRank(rangRequirement),
        range,
        shaping,
        world,
        id
    )
}