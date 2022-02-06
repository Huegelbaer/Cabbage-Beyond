package com.cabbagebeyond.data

import com.cabbagebeyond.model.World

interface WorldDataSource {

    //fun observeWorlds(): LiveData<Result<List<World>>>

    //fun observeWorld(id: String): LiveData<Result<World>>

    suspend fun getWorlds(): Result<List<World>>

    suspend fun getWorld(id: String): Result<World>

    suspend fun saveWorld(world: World): Result<Boolean>

    suspend fun deleteWorld(id: String): Result<Boolean>

    /*
        REMOTE
     */

    suspend fun refreshWorlds()

    suspend fun refreshWorld(id: String)
}