package com.cabbagebeyond.data

import com.cabbagebeyond.model.Handicap

interface HandicapDataSource {
    
    //fun observeHandicaps(): LiveData<Result<List<Handicap>>>

    suspend fun getHandicaps(): Result<List<Handicap>>

    suspend fun getHandicaps(ids: List<String>): Result<List<Handicap>>

    //suspend fun refreshHandicaps()

    //fun observeHandicap(id: String): LiveData<Result<Handicap>>

    suspend fun getHandicap(id: String): Result<Handicap>

    //suspend fun refreshHandicap(id: String)

    suspend fun saveHandicap(handicap: Handicap): Result<Boolean>

    //suspend fun deleteAllHandicaps()

    suspend fun deleteHandicap(id: String): Result<Boolean>
}