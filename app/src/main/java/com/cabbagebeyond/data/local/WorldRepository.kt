package com.cabbagebeyond.data.local

import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.data.dto.WorldDTO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorldRepository(
    private val worldDao: WorldDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : WorldDataSource {

    override suspend fun getWorlds(): Result<List<WorldDTO>> = withContext(ioDispatcher) {
        return@withContext worldDao.getWorlds()
    }

    override suspend fun getWorld(id: String): Result<WorldDTO> = withContext(ioDispatcher) {
        return@withContext worldDao.getWorld(id)
    }

    override suspend fun saveWorld(world: WorldDTO) = withContext(ioDispatcher) {
        worldDao.saveWorld(world)
    }

    override suspend fun deleteWorld(id: String) = withContext(ioDispatcher) {
        worldDao.deleteWorld(id)
    }
}