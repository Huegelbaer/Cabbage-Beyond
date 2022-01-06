package com.cabbagebeyond.data

import com.cabbagebeyond.model.Talent

interface TalentDataSource {

    //fun observeTalents(): LiveData<Result<List<Talent>>>

    suspend fun getTalents(): Result<List<Talent>>

    //suspend fun refreshTalents()

    //fun observeTalent(id: String): LiveData<Result<Talent>>

    suspend fun getTalent(id: String): Result<Talent>

    //suspend fun refreshTalent(id: String)

    suspend fun saveTalent(talent: Talent)

    //suspend fun deleteAllTalents()

    suspend fun deleteTalent(id: String)
}