package com.cabbagebeyond.data

import com.cabbagebeyond.model.Talent

interface TalentDataSource {

    //fun observeTalents(): LiveData<Result<List<Talent>>>

    //fun observeTalent(id: String): LiveData<Result<Talent>>

    suspend fun getTalents(): Result<List<Talent>>

    suspend fun getTalents(ids: List<String>): Result<List<Talent>>

    suspend fun getTalent(id: String): Result<Talent>

    suspend fun saveTalent(talent: Talent): Result<Boolean>

    suspend fun deleteTalent(id: String): Result<Boolean>

    /*
        REMOTE
    */

    suspend fun refreshTalents(): Result<Boolean>

    suspend fun refreshTalent(id: String): Result<Boolean>
}