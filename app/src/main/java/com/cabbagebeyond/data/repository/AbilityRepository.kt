package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.AbilityDataSource
import com.cabbagebeyond.data.remote.dto.AbilityDTO
import com.cabbagebeyond.data.local.dao.AbilityDao
import com.cabbagebeyond.data.local.entities.AbilityEntity
import com.cabbagebeyond.data.local.relations.asDomainModel
import com.cabbagebeyond.data.remote.service.AbilityService
import com.cabbagebeyond.model.Ability
import com.cabbagebeyond.model.Attribute
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AbilityRepository(
    private val abilityDao: AbilityDao,
    private val abilityService: AbilityService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AbilityDataSource {

    override suspend fun getAbilities(): Result<List<Ability>> = withContext(ioDispatcher) {
        val result = abilityDao.getAbilities()
        val abilities = result.map { it.asDomainModel() }
        return@withContext Result.success(abilities)
    }

    override suspend fun getAbilities(ids: List<String>): Result<List<Ability>> = withContext(ioDispatcher) {
        val result = abilityDao.getAbilities(ids)
        val abilities = result.map { it.asDomainModel() }
        return@withContext Result.success(abilities)
    }

    override suspend fun getAbility(id: String): Result<Ability> = withContext(ioDispatcher) {
        val result = abilityDao.getAbility(id)
        return@withContext Result.success(result.asDomainModel())
    }

    override suspend fun saveAbility(ability: Ability): Result<Boolean> = withContext(ioDispatcher) {
        abilityDao.saveAbility(ability.asDatabaseModel())
        return@withContext Result.success(true)
    }

    override suspend fun deleteAbility(ability: Ability): Result<Boolean> = withContext(ioDispatcher) {
        abilityDao.deleteAbility(ability.asDatabaseModel())
        return@withContext Result.success(true)
    }

    override suspend fun refreshAbilities() = withContext(ioDispatcher) {
        val result = abilityService.refreshAbilities()
        if (result.isSuccess) {
            result.getOrNull()?.forEach {
                abilityDao.saveAbility(it.asDatabaseModel())
            }
        }
    }

    override suspend fun refreshAbility(id: String) = withContext(ioDispatcher) {
        abilityService.refreshAbility(id)
        val result = abilityService.refreshAbility(id)
        if (result.isSuccess) {
            result.getOrNull()?.let {
                abilityDao.saveAbility(it.asDatabaseModel())
            }
        }
    }
}

private fun AbilityDTO.asDatabaseModel(): AbilityEntity {
    val attribute = valueToAbilityAttribute(attribute)
    return AbilityEntity(name, description, attribute, world, id)
}

private fun Ability.asDatabaseModel(): AbilityEntity {
    return AbilityEntity(name, description, attribute.asDatabaseModel(), world?.id ?: "", id)
}

private fun valueToAbilityAttribute(dtoValue: String?): AbilityEntity.Attribute {
    return when(dtoValue) {
        "Stärke" -> AbilityEntity.Attribute.STRENGTH
        "Verstand" -> AbilityEntity.Attribute.INTELLECT
        "Konstitution" -> AbilityEntity.Attribute.CONSTITUTION
        "Geschicklichkeit" -> AbilityEntity.Attribute.DEXTERITY
        "Willenskraft" -> AbilityEntity.Attribute.WILLPOWER
        else -> AbilityEntity.Attribute.UNKNOWN
    }
}

private fun Attribute.asDatabaseModel(): AbilityEntity.Attribute {
    return when(this) {
        Attribute.STRENGTH -> AbilityEntity.Attribute.STRENGTH
        Attribute.INTELLECT -> AbilityEntity.Attribute.INTELLECT
        Attribute.CONSTITUTION -> AbilityEntity.Attribute.CONSTITUTION
        Attribute.DEXTERITY -> AbilityEntity.Attribute.DEXTERITY
        Attribute.WILLPOWER -> AbilityEntity.Attribute.WILLPOWER
    }
}