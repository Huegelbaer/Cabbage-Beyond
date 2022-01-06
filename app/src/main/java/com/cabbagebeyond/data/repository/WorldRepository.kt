package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.data.dao.WorldDao
import com.cabbagebeyond.data.dto.asDatabaseModel
import com.cabbagebeyond.data.dto.asDomainModel
import com.cabbagebeyond.model.World
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorldRepository(
    private val worldDao: WorldDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : WorldDataSource {

    override suspend fun getWorlds(): Result<List<World>> = withContext(ioDispatcher) {
        val result = worldDao.getWorlds()
        return@withContext result.mapCatching { it.asDomainModel() }
    }

    override suspend fun getWorld(id: String): Result<World> = withContext(ioDispatcher) {
        val result = worldDao.getWorld(id)
        return@withContext result.mapCatching { it.asDomainModel() }
    }

    override suspend fun saveWorld(world: World) = withContext(ioDispatcher) {
        worldDao.saveWorld(world.asDatabaseModel())
    }

    override suspend fun deleteWorld(id: String) = withContext(ioDispatcher) {
        worldDao.deleteWorld(id)
    }
}