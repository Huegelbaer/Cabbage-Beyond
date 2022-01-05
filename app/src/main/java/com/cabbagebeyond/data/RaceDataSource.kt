package com.cabbagebeyond.data

import com.cabbagebeyond.data.dto.RaceDTO

interface RaceDataSource {

    //fun observeRaces(): LiveData<Result<List<RaceDTO>>>

    suspend fun getRaces(): Result<List<RaceDTO>>

    //suspend fun refreshRaces()

    //fun observeRace(id: String): LiveData<Result<RaceDTO>>

    suspend fun getRace(id: String): Result<RaceDTO>

    //suspend fun refreshRace(id: String)

    suspend fun saveRace(world: RaceDTO)

    //suspend fun deleteAllRaces()

    suspend fun deleteRace(id: String)
}