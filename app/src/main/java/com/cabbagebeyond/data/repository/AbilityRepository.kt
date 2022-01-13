package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.AbilityDataSource
import com.cabbagebeyond.data.dao.AbilityDao
import com.cabbagebeyond.data.dto.asDatabaseModel
import com.cabbagebeyond.data.dto.asDomainModel
import com.cabbagebeyond.model.Ability
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AbilityRepository(
    private val abilityDao: AbilityDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AbilityDataSource {

    override suspend fun getAbilities(): Result<List<Ability>> = withContext(ioDispatcher) {
        return@withContext abilityDao.getAbilities().mapCatching { it.asDomainModel() }
    }

    override suspend fun getAbilities(ids: List<String>): Result<List<Ability>> = withContext(ioDispatcher) {
        return@withContext abilityDao.getAbilities(ids).mapCatching { it.asDomainModel() }
    }

    override suspend fun getAbility(id: String): Result<Ability> = withContext(ioDispatcher){
        return@withContext abilityDao.getAbility(id).mapCatching { it.asDomainModel() }
    }

    override suspend fun saveAbility(ability: Ability) = withContext(ioDispatcher) {
        abilityDao.saveAbility(ability.asDatabaseModel())
    }

    override suspend fun deleteAbility(id: String) = withContext(ioDispatcher) {
        abilityDao.deleteAbility(id)
    }
}