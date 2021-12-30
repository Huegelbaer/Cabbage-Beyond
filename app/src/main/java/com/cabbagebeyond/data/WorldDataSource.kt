package com.cabbagebeyond.data

import androidx.lifecycle.LiveData
import com.cabbagebeyond.data.dto.WorldDTO

interface WorldDataSource {

    fun observeWorlds(): LiveData<Result<List<WorldDTO>>>

    suspend fun getWorlds(): Result<List<WorldDTO>>

    suspend fun refreshWorlds()

    fun observeWorld(id: String): LiveData<Result<WorldDTO>>

    suspend fun getWorld(id: String): Result<WorldDTO>

    suspend fun refreshWorld(id: String)

    suspend fun saveWorld(world: WorldDTO)

    suspend fun deleteAllWorlds()

    suspend fun deleteWorld(id: String)
}