package com.cabbagebeyond.data

import com.cabbagebeyond.data.dto.HandicapDTO

interface HandicapDataSource {
    
    //fun observeHandicaps(): LiveData<Result<List<HandicapDTO>>>

    suspend fun getHandicaps(): Result<List<HandicapDTO>>

    //suspend fun refreshHandicaps()

    //fun observeHandicap(id: String): LiveData<Result<HandicapDTO>>

    suspend fun getHandicap(id: String): Result<HandicapDTO>

    //suspend fun refreshHandicap(id: String)

    suspend fun saveHandicap(handicap: HandicapDTO)

    //suspend fun deleteAllHandicaps()

    suspend fun deleteHandicap(id: String)
}