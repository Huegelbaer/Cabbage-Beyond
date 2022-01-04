package com.cabbagebeyond.data.local

import com.cabbagebeyond.data.RaceDataSource
import com.cabbagebeyond.data.dto.RaceDTO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RaceRepository(
    private val raceDao: RaceDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RaceDataSource {

    override suspend fun getRaces(): Result<List<RaceDTO>> = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun getRace(id: String): Result<RaceDTO> = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun saveRace(world: RaceDTO) = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRace(id: String) = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }
}