package com.cabbagebeyond.data

import com.cabbagebeyond.model.Race

interface RaceDataSource {

    //fun observeRaces(): LiveData<Result<List<Race>>>

    //fun observeRace(id: String): LiveData<Result<Race>>

    suspend fun getRaces(): Result<List<Race>>

    suspend fun getRace(id: String): Result<Race>

    suspend fun saveRace(race: Race): Result<Boolean>

    suspend fun deleteRace(race: Race): Result<Boolean>

    /*
        REMOTE
    */

    suspend fun refreshRaces()

    suspend fun refreshRace(id: String)

}