package com.cabbagebeyond.data

import com.cabbagebeyond.model.World

interface WorldDataSource {

    //fun observeWorlds(): LiveData<Result<List<World>>>

    suspend fun getWorlds(): Result<List<World>>

    suspend fun refreshWorlds()

    //fun observeWorld(id: String): LiveData<Result<World>>

    suspend fun getWorld(id: String): Result<World>

    suspend fun refreshWorld(id: String)

    suspend fun saveWorld(world: World): Result<Boolean>

    //suspend fun deleteAllWorlds()

    suspend fun deleteWorld(id: String): Result<Boolean>
}