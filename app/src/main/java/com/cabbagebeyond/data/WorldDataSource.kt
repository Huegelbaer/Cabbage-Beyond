package com.cabbagebeyond.data

import com.cabbagebeyond.model.World

interface WorldDataSource {

    suspend fun getWorlds(): Result<List<World>>

    suspend fun getWorld(id: String): Result<World>

    suspend fun saveWorld(world: World): Result<Boolean>

    suspend fun deleteWorld(world: World): Result<Boolean>

    /*
        REMOTE
     */

    suspend fun refreshWorlds()

    suspend fun refreshWorld(id: String)
}