package com.cabbagebeyond.data

import com.cabbagebeyond.model.Talent

interface TalentDataSource {

    suspend fun getTalents(): Result<List<Talent>>

    suspend fun getTalents(ids: List<String>): Result<List<Talent>>

    suspend fun getTalent(id: String): Result<Talent>

    suspend fun saveTalent(talent: Talent): Result<Boolean>

    suspend fun deleteTalent(talent: Talent): Result<Boolean>

    /*
        REMOTE
    */

    suspend fun refreshTalents()

    suspend fun refreshTalent(id: String)
}