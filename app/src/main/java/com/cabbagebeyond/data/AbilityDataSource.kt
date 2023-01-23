package com.cabbagebeyond.data

import com.cabbagebeyond.model.Ability

interface AbilityDataSource {

    suspend fun getAbilities(): Result<List<Ability>>

    suspend fun getAbilities(ids: List<String>): Result<List<Ability>>

    suspend fun getAbility(id: String): Result<Ability>

    suspend fun saveAbility(ability: Ability): Result<Boolean>

    suspend fun deleteAbility(ability: Ability): Result<Boolean>

    /*
        REMOTE
     */

    suspend fun refreshAbilities()

    suspend fun refreshAbility(id: String)
}