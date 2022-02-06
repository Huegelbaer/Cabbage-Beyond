package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.AbilityDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.data.dao.AbilityDao
import com.cabbagebeyond.data.dto.AbilityDTO
import com.cabbagebeyond.data.remote.AbilityService
import com.cabbagebeyond.model.Ability
import com.cabbagebeyond.model.World
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AbilityRepository(
    private val abilityDao: AbilityDao,
    private val abilityService: AbilityService,
    private val worldDataSource: WorldDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AbilityDataSource {

    override suspend fun getAbilities(): Result<List<Ability>> = withContext(ioDispatcher) {
        val result = abilityDao.getAbilities()
        return@withContext mapList(result)
    }

    override suspend fun getAbilities(ids: List<String>): Result<List<Ability>> = withContext(ioDispatcher) {
        val result = abilityDao.getAbilities(ids)
        return@withContext mapList(result)
    }

    override suspend fun getAbility(id: String): Result<Ability> = withContext(ioDispatcher) {
        val result = abilityDao.getAbility(id)
        return@withContext map(result)
    }

    override suspend fun saveAbility(ability: Ability): Result<Boolean> = withContext(ioDispatcher) {
        abilityDao.saveAbility(ability.asDatabaseModel())
    }

    override suspend fun deleteAbility(id: String): Result<Boolean> = withContext(ioDispatcher) {
        abilityDao.deleteAbility(id)
    }

    override suspend fun refreshAbilities(): Result<Boolean> = withContext(ioDispatcher) {
        abilityService.refreshAbilities()
    }

    override suspend fun refreshAbility(id: String): Result<Boolean> = withContext(ioDispatcher) {
        abilityService.refreshAbility(id)
    }

    private suspend fun mapList(result: Result<List<AbilityDTO>>): Result<List<Ability>> {
        val worlds = worldDataSource.getWorlds().getOrDefault(listOf())
        return result.mapCatching {
            it.asDomainModel(worlds)
        }
    }

    private suspend fun map(result: Result<AbilityDTO>): Result<Ability> {
        return result.mapCatching {
            val world = worldDataSource.getWorld(it.world).getOrNull()
            it.asDomainModel(world)
        }
    }
}

fun List<AbilityDTO>.asDomainModel(allWorlds: List<World>): List<Ability> {
    return map { ability ->
        val world = allWorlds.firstOrNull { it.id == ability.world }
        ability.asDomainModel(world)
    }
}

fun AbilityDTO.asDomainModel(world: World?): Ability {
    return Ability(name, description, attribute, world, id)
}

fun List<Ability>.asDatabaseModel(): List<AbilityDTO> {
    return map {
        it.asDatabaseModel()
    }
}

fun Ability.asDatabaseModel(): AbilityDTO {
    return AbilityDTO(name, description, attribute, world?.id ?: "", id)
}