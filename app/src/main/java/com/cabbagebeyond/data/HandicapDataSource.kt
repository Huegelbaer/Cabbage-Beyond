package com.cabbagebeyond.data

import com.cabbagebeyond.model.Handicap

interface HandicapDataSource {
    
    //fun observeHandicaps(): LiveData<Result<List<Handicap>>>

    //fun observeHandicap(id: String): LiveData<Result<Handicap>>

    suspend fun getHandicaps(): Result<List<Handicap>>

    suspend fun getHandicaps(ids: List<String>): Result<List<Handicap>>

    suspend fun getHandicap(id: String): Result<Handicap>

    suspend fun saveHandicap(handicap: Handicap): Result<Boolean>

    suspend fun deleteHandicap(id: String): Result<Boolean>

    /*
        REMOTE
     */

    suspend fun refreshHandicaps(): Result<Boolean>

    suspend fun refreshHandicap(id: String): Result<Boolean>

}