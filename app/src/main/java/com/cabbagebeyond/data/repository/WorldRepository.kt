package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.data.dto.asDomainModel
import com.cabbagebeyond.data.local.dao.WorldDao
import com.cabbagebeyond.data.local.entities.asDatabaseModel
import com.cabbagebeyond.data.local.entities.asDomainModel
import com.cabbagebeyond.data.remote.WorldService
import com.cabbagebeyond.model.World
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorldRepository(
    private val worldDao: WorldDao,
    private val worldService: WorldService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : WorldDataSource {

    override suspend fun getWorlds(): Result<List<World>> = withContext(ioDispatcher) {
        val result = worldDao.getWorlds()
        val list = result.map { it.asDomainModel() }
        return@withContext Result.success(list)
    }

    override suspend fun getWorld(id: String): Result<World> = withContext(ioDispatcher) {
        val result = worldDao.getWorld(id)
        return@withContext Result.success(result.asDomainModel())
    }

    override suspend fun saveWorld(world: World): Result<Boolean> = withContext(ioDispatcher) {
        worldDao.saveWorld(world.asDatabaseModel())
        return@withContext Result.success(true)
    }

    override suspend fun deleteWorld(world: World): Result<Boolean> = withContext(ioDispatcher) {
        worldDao.deleteWorld(world.asDatabaseModel())
        return@withContext Result.success(true)
    }

    override suspend fun refreshWorlds() = withContext(ioDispatcher) {
        val result = worldService.refreshWorlds()
        if (result.isSuccess) {
            result.getOrNull()?.forEach {
                worldDao.saveWorld(it.asDomainModel().asDatabaseModel())
            }
        }
    }

    override suspend fun refreshWorld(id: String) {
        val result = worldService.refreshWorld(id)
        if (result.isSuccess) {
            result.getOrNull()?.let {
                worldDao.saveWorld(it.asDomainModel().asDatabaseModel())
            }
        }
    }
}