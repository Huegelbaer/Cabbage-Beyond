package com.cabbagebeyond.data

import com.cabbagebeyond.data.dto.AbilityDTO

interface AbilityDataSource {

    //fun observeAbilities(): LiveData<Result<List<AbilityDTO>>>

    suspend fun getAbilities(): Result<List<AbilityDTO>>

    //suspend fun refreshAbilities()

    //fun observeAbility(id: String): LiveData<Result<AbilityDTO>>

    suspend fun getAbility(id: String): Result<AbilityDTO>

    //suspend fun refreshAbility(id: String)

    suspend fun saveAbility(ability: AbilityDTO)

    //suspend fun deleteAllAbilities()

    suspend fun deleteAbility(id: String)
}