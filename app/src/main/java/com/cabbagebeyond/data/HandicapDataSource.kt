package com.cabbagebeyond.data

import com.cabbagebeyond.model.Handicap

interface HandicapDataSource {

    suspend fun getHandicaps(): Result<List<Handicap>>

    suspend fun getHandicaps(ids: List<String>): Result<List<Handicap>>

    suspend fun getHandicap(id: String): Result<Handicap>

    suspend fun saveHandicap(handicap: Handicap): Result<Boolean>

    suspend fun deleteHandicap(handicap: Handicap): Result<Boolean>

    /*
        REMOTE
     */

    suspend fun refreshHandicaps()

    suspend fun refreshHandicap(id: String)

}