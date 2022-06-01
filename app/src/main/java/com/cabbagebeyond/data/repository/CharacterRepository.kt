package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.*
import com.cabbagebeyond.data.dao.CharacterDao
import com.cabbagebeyond.data.dto.CharacterDTO
import com.cabbagebeyond.data.remote.CharacterService
import com.cabbagebeyond.model.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterRepository(
    private val characterDao: CharacterDao,
    private val characterService: CharacterService,
    private val abilityDataSource: AbilityDataSource,
    private val equipmentDataSource: EquipmentDataSource,
    private val forceDataSource: ForceDataSource,
    private val handicapDataSource: HandicapDataSource,
    private val raceDataSource: RaceDataSource,
    private val talentDataSource: TalentDataSource,
    private val worldDataSource: WorldDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): CharacterDataSource {

    override suspend fun getCharacters(): Result<List<Character>> = withContext(ioDispatcher) {
        val characters = characterDao.getCharacters()
        return@withContext mapList(characters)
    }

    override suspend fun getCharactersSortedByName(): Result<List<Character>> = withContext(ioDispatcher) {
        val characters = characterDao.getCharactersOrderedByName()
        return@withContext mapList(characters)
    }

    override suspend fun getCharactersSortedByRace(): Result<List<Character>> = withContext(ioDispatcher) {
        val characters = characterDao.getCharactersOrderedByRace()
        return@withContext mapList(characters)
    }

    override suspend fun getCharactersSortedByType(): Result<List<Character>> = withContext(ioDispatcher) {
        val characters = characterDao.getCharactersOrderedByType()
        return@withContext mapList(characters)
    }

    override suspend fun getCharactersSortedByWorld(): Result<List<Character>> = withContext(ioDispatcher) {
        val characters = characterDao.getCharactersOrderedByWorld()
        return@withContext mapList(characters)
    }

    override suspend fun getCharactersOfUser(id: String): Result<List<Character>> = withContext(ioDispatcher) {
        val characters = characterDao.getCharactersOfUser(id)
        return@withContext mapList(characters)
    }

    override suspend fun getCharacter(id: String): Result<Character> = withContext(ioDispatcher) {
        val character = characterDao.getCharacter(id)
        return@withContext map(character)
    }

    override suspend fun saveCharacter(character: Character): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext characterDao.saveCharacter(character.asDatabaseModel())
    }

    override suspend fun deleteCharacter(id: String): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext characterDao.deleteCharacter(id)
    }

    override suspend fun refreshCharacters(): Result<Boolean> = withContext(ioDispatcher) {
        characterService.refreshCharacters()
    }

    override suspend fun refreshCharacter(id: String): Result<Boolean> = withContext(ioDispatcher) {
        characterService.refreshCharacter(id)
    }


    private suspend fun mapList(result: Result<List<CharacterDTO>>): Result<List<Character>> {
        val allAbilities = abilityDataSource.getAbilities().getOrDefault(listOf())
        val allEquipments = equipmentDataSource.getEquipments().getOrDefault(listOf())
        val allForces = forceDataSource.getForces().getOrDefault(listOf())
        val allHandicaps = handicapDataSource.getHandicaps().getOrDefault(listOf())
        val allRaces = raceDataSource.getRaces().getOrDefault(listOf())
        val allTalents = talentDataSource.getTalents().getOrDefault(listOf())
        val allWorlds = worldDataSource.getWorlds().getOrDefault(listOf())
        return result.mapCatching {
            it.asDomainModel(allRaces, allAbilities, allEquipments, allForces, allHandicaps, allTalents, allWorlds)
        }
    }

    private suspend fun map(result: Result<CharacterDTO>): Result<Character> {
        val allAbilities = abilityDataSource.getAbilities().getOrDefault(listOf())
        val allEquipments = equipmentDataSource.getEquipments().getOrDefault(listOf())
        val allForces = forceDataSource.getForces().getOrDefault(listOf())
        val allHandicaps = handicapDataSource.getHandicaps().getOrDefault(listOf())
        val allRaces = raceDataSource.getRaces().getOrDefault(listOf())
        val allTalents = talentDataSource.getTalents().getOrDefault(listOf())
        val allWorlds = worldDataSource.getWorlds().getOrDefault(listOf())
        return result.mapCatching { character ->
            character.asDomainModel(allRaces.first { it.id == character.race }, allAbilities, allEquipments, allForces, allHandicaps, allTalents, allWorlds.first { it.id == character.world })
        }
    }
}

fun List<CharacterDTO>.asDomainModel(allRaces: List<Race>, allAbilities: List<Ability>, allEquipments: List<Equipment>, allForces: List<Force>, allHandicaps: List<Handicap>, allTalents: List<Talent>, allWorlds: List<World>): List<Character> {
    return map { character ->
        character.asDomainModel(
            allRaces.firstOrNull { it.id == character.race },
            allAbilities.filter { it.id in character.abilities },
            allEquipments.filter { it.id in character.equipments },
            allForces.filter { it.id in character.forces },
            allHandicaps.filter { it.id in character.handicaps },
            allTalents.filter { it.id in character.talents },
            allWorlds.firstOrNull { it.id == character.world }
        )
    }
}

fun CharacterDTO.asDomainModel(_race: Race?, _abilities: List<Ability>, _equipments: List<Equipment>, _forces: List<Force>, _handicaps: List<Handicap>, _talents: List<Talent>, _world: World?): Character {
    return Character(name, _race, description, charisma, constitution, deception, dexterity, intelligence, investigation, perception, stealth, strength, willpower, movement, parry, toughness, _abilities, _equipments, _forces, _handicaps, _talents, valueToCharacterType(type), owner, _world, id)
}

fun List<Character>.asDatabaseModel(): List<CharacterDTO> {
    return map {
        it.asDatabaseModel()
    }
}

fun Character.asDatabaseModel(): CharacterDTO {
    return CharacterDTO(name, race?.id, description, charisma, constitution, deception, dexterity, intelligence, investigation, perception, stealth, strength, willpower, movement, parry, toughness, abilities.map { it.id }, equipments.map { it.id }, forces.map { it.id }, handicaps.map { it.id }, talents.map { it.id }, type?.asDatabaseModel() ?: "", owner, world?.id, id)
}

fun valueToCharacterType(dtoValue: String?): Character.Type? {
    return when(dtoValue) {
        "Spieler" -> Character.Type.PLAYER
        "NPC" -> Character.Type.NPC
        "Monster" -> Character.Type.MONSTER
        else -> null
    }
}

fun Character.Type.asDatabaseModel(): String {
    return when(this) {
        Character.Type.PLAYER -> "Spieler"
        Character.Type.NPC -> "NPC"
        Character.Type.MONSTER -> "Monster"
    }
}
