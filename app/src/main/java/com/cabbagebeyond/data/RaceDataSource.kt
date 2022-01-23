package com.cabbagebeyond.data

import com.cabbagebeyond.model.Race

interface RaceDataSource {

    //fun observeRaces(): LiveData<Result<List<Race>>>

    suspend fun getRaces(): Result<List<Race>>

    //suspend fun refreshRaces()

    //fun observeRace(id: String): LiveData<Result<Race>>

    suspend fun getRace(id: String): Result<Race>

    //suspend fun refreshRace(id: String)

    suspend fun saveRace(race: Race): Result<Boolean>

    //suspend fun deleteAllRaces()

    suspend fun deleteRace(id: String): Result<Boolean>
}