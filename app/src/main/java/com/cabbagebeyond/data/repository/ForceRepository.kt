package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.ForceDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.data.dao.ForceDao
import com.cabbagebeyond.data.dto.ForceDTO
import com.cabbagebeyond.data.remote.ForceService
import com.cabbagebeyond.model.Force
import com.cabbagebeyond.model.World
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
        val forces = forceDao.getForces()
        return@withContext mapList(forces)
    }

    override suspend fun getForces(ids: List<String>): Result<List<Force>> = withContext(ioDispatcher) {
        val forces = forceDao.getForces(ids)
        return@withContext mapList(forces)
    }

    override suspend fun getForce(id: String): Result<Force> = withContext(ioDispatcher) {
        val force = forceDao.getForce(id)
        return@withContext map(force)
    }

    override suspend fun saveForce(force: Force): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext forceDao.saveForce(force.asDatabaseModel())
    }

    override suspend fun deleteForce(id: String): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext forceDao.deleteForce(id)
    }

    override suspend fun refreshForces(): Result<Boolean> = withContext(ioDispatcher) {
        forceService.refreshForces()
    }

    override suspend fun refreshForce(id: String): Result<Boolean> = withContext(ioDispatcher) {
        forceService.refreshForce(id)
    }

    private suspend fun mapList(result: Result<List<ForceDTO>>): Result<List<Force>> {
        val worlds = worldDataSource.getWorlds().getOrDefault(listOf())
        return result.mapCatching {
            it.asDomainModel(worlds)
        }
    }

    private suspend fun map(result: Result<ForceDTO>): Result<Force> {
        return result.mapCatching {
            val world = worldDataSource.getWorld(it.world).getOrNull()
            it.asDomainModel(world)
        }
    }
}


fun List<ForceDTO>.asDomainModel(worlds: List<World>): List<Force> {
    return map { force ->
        force.asDomainModel(worlds.firstOrNull { it.id == force.world })
    }
}

fun ForceDTO.asDomainModel(world: World?): Force {
    return Force(name, description, cost, duration, rangRequirement, range, shaping, world, id)
}

fun List<Force>.asDatabaseModel(): List<ForceDTO> {
    return map {
        it.asDatabaseModel()
    }
}

fun Force.asDatabaseModel(): ForceDTO {
    return ForceDTO(name, description, cost, duration, rangRequirement, range, shaping, world?.id ?: "", id)
}