package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.TalentDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.data.local.dao.TalentDao
import com.cabbagebeyond.data.dto.TalentDTO
import com.cabbagebeyond.data.remote.TalentService
import com.cabbagebeyond.model.Rank
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
    return Talent(name, description, valueToTalentRank(rangRequirement), requirements.split(", "), valueToTalentType(type), world, id)
}

fun List<Talent>.asDatabaseModel(): List<TalentDTO> {
    return map {
        it.asDatabaseModel()
    }
}

fun Talent.asDatabaseModel(): TalentDTO {
    return TalentDTO(name, description, rangRequirement?.asDatabaseModel() ?: "", requirements.joinToString(), type?.asDatabaseModel() ?: "", world?.id ?: "", id)
}

fun valueToTalentRank(dtoValue: String?): Rank? {
    return when(dtoValue) {
        "Anfänger" -> Rank.ROOKIE
        "Fortgeschritten" -> Rank.ADVANCED
        "Veteran" -> Rank.VETERAN
        "Held" -> Rank.HERO
        "Legende" -> Rank.LEGEND
        else -> null
    }
}

fun Rank.asDatabaseModel(): String {
    return when(this) {
        Rank.ROOKIE -> "Anfänger"
        Rank.ADVANCED -> "Fortgeschritten"
        Rank.VETERAN -> "Veteran"
        Rank.HERO -> "Held"
        Rank.LEGEND -> "Legende"
    }
}

fun valueToTalentType(dtoValue: String?): Talent.Type? {
    return when(dtoValue) {
        "Hintergrundtalent" -> Talent.Type.BACKGROUND
        "Anführertalent" -> Talent.Type.LEADER
        "Expertentalent" -> Talent.Type.EXPERT
        "Kampftalent" -> Talent.Type.FIGHT
        "Machttalent" -> Talent.Type.FORCE
        "Rassentalent" -> Talent.Type.RACE
        "Soziales Talent" -> Talent.Type.SOCIAL
        "Übernatürliches Talent" -> Talent.Type.SUPERNATURAL
        "Wildcard Talent" -> Talent.Type.WILDCARD
        "Legendäres Talent" -> Talent.Type.LEGENDARY
        "Drachentalent" -> Talent.Type.DRAGON
        "Athletiktalent" -> Talent.Type.ATHLETIC
        "Wahrnehmungstalent" -> Talent.Type.PERCEPTION
        "Täuschungstalent" -> Talent.Type.ILLUSION
        "Provozierentalent" -> Talent.Type.PROVOKE
        "Diebeskunsttalent" -> Talent.Type.THIEVERY
        "Heimlichkeitstalent" -> Talent.Type.STEALTH
        "Reviertalent" -> Talent.Type.TERRITORY
        "Verstandstalent" -> Talent.Type.INTELLECT
        "Krafttalent" -> Talent.Type.STRENGTH
        else -> null
    }
}

fun Talent.Type.asDatabaseModel(): String {
    return when(this) {
        Talent.Type.BACKGROUND -> "Hintergrundtalent"
        Talent.Type.LEADER -> "Anführertalent"
        Talent.Type.EXPERT -> "Expertentalent"
        Talent.Type.FIGHT -> "Kampftalent"
        Talent.Type.FORCE -> "Machttalent"
        Talent.Type.RACE -> "Rassentalent"
        Talent.Type.SOCIAL -> "Soziales Talent"
        Talent.Type.SUPERNATURAL -> "Übernatürliches Talent"
        Talent.Type.WILDCARD -> "Wildcard Talent"
        Talent.Type.LEGENDARY -> "Legendäres Talent"
        Talent.Type.DRAGON -> "Drachentalent"
        Talent.Type.ATHLETIC -> "Athletiktalent"
        Talent.Type.PERCEPTION -> "Wahrnehmungstalent"
        Talent.Type.ILLUSION -> "Täuschungstalent"
        Talent.Type.PROVOKE -> "Provozierentalent"
        Talent.Type.THIEVERY -> "Diebeskunsttalent"
        Talent.Type.STEALTH -> "Heimlichkeitstalent"
        Talent.Type.TERRITORY -> "Reviertalent"
        Talent.Type.INTELLECT -> "Verstandstalent"
        Talent.Type.STRENGTH -> "Krafttalent"
    }
}