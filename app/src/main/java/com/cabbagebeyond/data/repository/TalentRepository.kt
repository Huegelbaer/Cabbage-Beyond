package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.TalentDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.data.dao.TalentDao
import com.cabbagebeyond.data.dto.TalentDTO
import com.cabbagebeyond.data.remote.TalentService
import com.cabbagebeyond.model.Talent
import com.cabbagebeyond.model.World
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TalentRepository(
    private val talentDao: TalentDao,
    private val talentService: TalentService,
    private val worldDataSource: WorldDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TalentDataSource {

    override suspend fun getTalents(): Result<List<Talent>> = withContext(ioDispatcher) {
        val result = talentDao.getTalents()
        return@withContext mapList(result)
    }

    override suspend fun getTalents(ids: List<String>): Result<List<Talent>> = withContext(ioDispatcher) {
        val result = talentDao.getTalents(ids)
        return@withContext mapList(result)
    }

    override suspend fun getTalent(id: String): Result<Talent> = withContext(ioDispatcher) {
        val result = talentDao.getTalent(id)
        return@withContext map(result)
    }

    override suspend fun saveTalent(talent: Talent): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext talentDao.saveTalent(talent.asDatabaseModel())
    }

    override suspend fun deleteTalent(id: String): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext talentDao.deleteTalent(id)
    }

    override suspend fun refreshTalents(): Result<Boolean> = withContext(ioDispatcher) {
        talentService.refreshTalents()
    }

    override suspend fun refreshTalent(id: String): Result<Boolean> = withContext(ioDispatcher) {
        talentService.refreshTalent(id)
    }

    private suspend fun mapList(result: Result<List<TalentDTO>>): Result<List<Talent>> {
        val worlds = worldDataSource.getWorlds().getOrDefault(listOf())
        return result.mapCatching {
            it.asDomainModel(worlds)
        }
    }

    private suspend fun map(result: Result<TalentDTO>): Result<Talent> {
        return result.mapCatching {
            val world = worldDataSource.getWorld(it.world).getOrNull()
            it.asDomainModel(world)
        }
    }
}


fun List<TalentDTO>.asDomainModel(worlds: List<World>): List<Talent> {
    return map { talent ->
        talent.asDomainModel(worlds.firstOrNull { it.id == talent.world })
    }
}

fun TalentDTO.asDomainModel(world: World?): Talent {
    return Talent(name, description, rangRequirement, requirements.split(", "), type, world, id)
}

fun List<Talent>.asDatabaseModel(): List<TalentDTO> {
    return map {
        it.asDatabaseModel()
    }
}

fun Talent.asDatabaseModel(): TalentDTO {
    return TalentDTO(name, description, rangRequirement, requirements.joinToString(), type, world?.id ?: "", id)
}