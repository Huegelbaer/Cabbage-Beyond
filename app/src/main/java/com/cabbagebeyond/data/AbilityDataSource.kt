package com.cabbagebeyond.data

import com.cabbagebeyond.model.Ability

interface AbilityDataSource {

    //fun observeAbilities(): LiveData<Result<List<Ability>>>

    suspend fun getAbilities(): Result<List<Ability>>

    suspend fun getAbilities(ids: List<String>): Result<List<Ability>>

    //suspend fun refreshAbilities()

    //fun observeAbility(id: String): LiveData<Result<Ability>>

    suspend fun getAbility(id: String): Result<Ability>

    //suspend fun refreshAbility(id: String)

    suspend fun saveAbility(ability: Ability): Result<Boolean>

    //suspend fun deleteAllAbilities()

    suspend fun deleteAbility(id: String): Result<Boolean>
}