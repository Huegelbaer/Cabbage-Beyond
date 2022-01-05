package com.cabbagebeyond.data

import com.cabbagebeyond.data.dto.TalentDTO

interface TalentDataSource {

    //fun observeTalents(): LiveData<Result<List<TalentDTO>>>

    suspend fun getTalents(): Result<List<TalentDTO>>

    //suspend fun refreshTalents()

    //fun observeTalent(id: String): LiveData<Result<TalentDTO>>

    suspend fun getTalent(id: String): Result<TalentDTO>

    //suspend fun refreshTalent(id: String)

    suspend fun saveTalent(talent: TalentDTO)

    //suspend fun deleteAllTalents()

    suspend fun deleteTalent(id: String)
}