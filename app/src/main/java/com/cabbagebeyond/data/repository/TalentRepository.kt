package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.TalentDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.data.remote.dto.TalentDTO
import com.cabbagebeyond.data.local.asDatabaseModel
import com.cabbagebeyond.data.local.asDomainModel
import com.cabbagebeyond.data.local.dao.TalentDao
import com.cabbagebeyond.data.local.entities.TalentEntity
import com.cabbagebeyond.data.local.entities.WorldEntity
import com.cabbagebeyond.data.local.relations.TalentWithWorld
import com.cabbagebeyond.data.local.valueToRank
import com.cabbagebeyond.data.remote.service.TalentService
import com.cabbagebeyond.model.Talent
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
        val list = result.map { it.asDomainModel() }
        return@withContext Result.success(list)
    }

    override suspend fun getTalents(ids: List<String>): Result<List<Talent>> =
        withContext(ioDispatcher) {
            val result = talentDao.getTalents(ids)
            val list = result.map { it.asDomainModel() }
            return@withContext Result.success(list)
        }

    override suspend fun getTalent(id: String): Result<Talent> = withContext(ioDispatcher) {
        val result = talentDao.getTalent(id)
        return@withContext Result.success(result.asDomainModel())
    }

    override suspend fun saveTalent(talent: Talent): Result<Boolean> = withContext(ioDispatcher) {
        talentDao.saveTalent(talent.asDatabaseModel())
        return@withContext Result.success(true)
    }

    override suspend fun deleteTalent(talent: Talent): Result<Boolean> = withContext(ioDispatcher) {
        talentDao.deleteTalent(talent.asDatabaseModel())
        return@withContext Result.success(true)
    }

    override suspend fun refreshTalents() = withContext(ioDispatcher) {
        val result = talentService.refreshTalents()
        if (result.isSuccess) {
            result.getOrNull()?.forEach {
                talentDao.saveTalent(it.asDatabaseModel())
            }
        }
    }

    override suspend fun refreshTalent(id: String) = withContext(ioDispatcher) {
        val result = talentService.refreshTalent(id)
        if (result.isSuccess) {
            result.getOrNull()?.let {
                talentDao.saveTalent(it.asDatabaseModel())
            }
        }
    }
}

private fun TalentDTO.asDatabaseModel(): TalentEntity {
    return TalentEntity(
        name,
        description,
        valueToRank(rangRequirement),
        requirements.split(", "),
        valueToTalentType(type),
        world,
        id
    )
}

private fun List<Talent>.asDatabaseModel(): List<TalentEntity> {
    return map {
        it.asDatabaseModel()
    }
}

private fun Talent.asDatabaseModel(): TalentEntity {
    return TalentEntity(
        name,
        description,
        rangRequirement.asDatabaseModel(),
        requirements,
        type.asDatabaseModel(),
        world?.id ?: "",
        id
    )
}

private fun List<TalentWithWorld>.asDomainModel(): List<Talent> {
    return map {
        it.asDomainModel()
    }
}

private fun TalentWithWorld.asDomainModel(): Talent {
    return talent.asDomainModel(world)
}

fun TalentEntity.asDomainModel(worldEntity: WorldEntity?): Talent {
    return Talent(
        name,
        description,
        requiredRank.asDomainModel(),
        requirements,
        type.asDomainModel(),
        worldEntity?.asDomainModel(),
        id
    )
}

private fun valueToTalentType(dtoValue: String?): TalentEntity.Type {
    return when (dtoValue) {
        "Hintergrundtalent" -> TalentEntity.Type.BACKGROUND
        "Anführertalent" -> TalentEntity.Type.LEADER
        "Expertentalent" -> TalentEntity.Type.EXPERT
        "Kampftalent" -> TalentEntity.Type.FIGHT
        "Machttalent" -> TalentEntity.Type.FORCE
        "Rassentalent" -> TalentEntity.Type.RACE
        "Soziales Talent" -> TalentEntity.Type.SOCIAL
        "Übernatürliches Talent" -> TalentEntity.Type.SUPERNATURAL
        "Wildcard Talent" -> TalentEntity.Type.WILDCARD
        "Legendäres Talent" -> TalentEntity.Type.LEGENDARY
        "Drachentalent" -> TalentEntity.Type.DRAGON
        "Athletiktalent" -> TalentEntity.Type.ATHLETIC
        "Wahrnehmungstalent" -> TalentEntity.Type.PERCEPTION
        "Täuschungstalent" -> TalentEntity.Type.ILLUSION
        "Provozierentalent" -> TalentEntity.Type.PROVOKE
        "Diebeskunsttalent" -> TalentEntity.Type.THIEVERY
        "Heimlichkeitstalent" -> TalentEntity.Type.STEALTH
        "Reviertalent" -> TalentEntity.Type.TERRITORY
        "Verstandstalent" -> TalentEntity.Type.INTELLECT
        "Krafttalent" -> TalentEntity.Type.STRENGTH
        else -> TalentEntity.Type.UNKNOWN
    }
}

private fun Talent.Type.asDatabaseModel(): TalentEntity.Type {
    return when (this) {
        Talent.Type.BACKGROUND -> TalentEntity.Type.BACKGROUND
        Talent.Type.LEADER -> TalentEntity.Type.LEADER
        Talent.Type.EXPERT -> TalentEntity.Type.EXPERT
        Talent.Type.FIGHT -> TalentEntity.Type.FIGHT
        Talent.Type.FORCE -> TalentEntity.Type.FORCE
        Talent.Type.RACE -> TalentEntity.Type.RACE
        Talent.Type.SOCIAL -> TalentEntity.Type.SOCIAL
        Talent.Type.SUPERNATURAL -> TalentEntity.Type.SUPERNATURAL
        Talent.Type.WILDCARD -> TalentEntity.Type.WILDCARD
        Talent.Type.LEGENDARY -> TalentEntity.Type.LEGENDARY
        Talent.Type.DRAGON -> TalentEntity.Type.DRAGON
        Talent.Type.ATHLETIC -> TalentEntity.Type.ATHLETIC
        Talent.Type.PERCEPTION -> TalentEntity.Type.PERCEPTION
        Talent.Type.ILLUSION -> TalentEntity.Type.ILLUSION
        Talent.Type.PROVOKE -> TalentEntity.Type.PROVOKE
        Talent.Type.THIEVERY -> TalentEntity.Type.THIEVERY
        Talent.Type.STEALTH -> TalentEntity.Type.STEALTH
        Talent.Type.TERRITORY -> TalentEntity.Type.TERRITORY
        Talent.Type.INTELLECT -> TalentEntity.Type.INTELLECT
        Talent.Type.STRENGTH -> TalentEntity.Type.STRENGTH
    }
}

private fun TalentEntity.Type.asDomainModel(): Talent.Type {
    return when (this) {
        TalentEntity.Type.BACKGROUND -> Talent.Type.BACKGROUND
        TalentEntity.Type.LEADER -> Talent.Type.LEADER
        TalentEntity.Type.EXPERT -> Talent.Type.EXPERT
        TalentEntity.Type.FIGHT -> Talent.Type.FIGHT
        TalentEntity.Type.FORCE -> Talent.Type.FORCE
        TalentEntity.Type.RACE -> Talent.Type.RACE
        TalentEntity.Type.SOCIAL -> Talent.Type.SOCIAL
        TalentEntity.Type.SUPERNATURAL -> Talent.Type.SUPERNATURAL
        TalentEntity.Type.WILDCARD -> Talent.Type.WILDCARD
        TalentEntity.Type.LEGENDARY -> Talent.Type.LEGENDARY
        TalentEntity.Type.DRAGON -> Talent.Type.DRAGON
        TalentEntity.Type.ATHLETIC -> Talent.Type.ATHLETIC
        TalentEntity.Type.PERCEPTION -> Talent.Type.PERCEPTION
        TalentEntity.Type.ILLUSION -> Talent.Type.ILLUSION
        TalentEntity.Type.PROVOKE -> Talent.Type.PROVOKE
        TalentEntity.Type.THIEVERY -> Talent.Type.THIEVERY
        TalentEntity.Type.STEALTH -> Talent.Type.STEALTH
        TalentEntity.Type.TERRITORY -> Talent.Type.TERRITORY
        TalentEntity.Type.INTELLECT -> Talent.Type.INTELLECT
        TalentEntity.Type.STRENGTH -> Talent.Type.STRENGTH
        TalentEntity.Type.UNKNOWN -> Talent.Type.BACKGROUND
    }
}