package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.AbilityDataSource
import com.cabbagebeyond.data.dto.AbilityDTO
import com.cabbagebeyond.data.dao.AbilityDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AbilityRepository(
    private val abilityDao: AbilityDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AbilityDataSource {

    override suspend fun getAbilities(): Result<List<AbilityDTO>> = withContext(ioDispatcher) {
        return@withContext abilityDao.getAbilities()
    }

    override suspend fun getAbility(id: String): Result<AbilityDTO> = withContext(ioDispatcher){
        return@withContext abilityDao.getAbility(id)
    }

    override suspend fun saveAbility(ability: AbilityDTO) = withContext(ioDispatcher) {
        abilityDao.saveAbility(ability)
    }

    override suspend fun deleteAbility(id: String) = withContext(ioDispatcher) {
        abilityDao.deleteAbility(id)
    }
}