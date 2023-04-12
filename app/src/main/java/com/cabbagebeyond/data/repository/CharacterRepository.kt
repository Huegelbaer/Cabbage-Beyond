package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.*
import com.cabbagebeyond.data.remote.dto.CharacterDTO
import com.cabbagebeyond.data.local.dao.CharacterDao
import com.cabbagebeyond.data.local.entities.CharacterEntity
import com.cabbagebeyond.data.local.relations.*
import com.cabbagebeyond.data.remote.service.CharacterService
import com.cabbagebeyond.model.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterRepository(
    private val characterDao: CharacterDao,
    private val characterService: CharacterService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CharacterDataSource {

    override suspend fun getCharacters(): Result<List<Character>> = withContext(ioDispatcher) {
        val result = characterDao.getCharacters()
        val characters = result.map { it.asDomainModel() }
        return@withContext Result.success(characters)
    }

    override suspend fun getCharactersSortedByName(): Result<List<Character>> =
        withContext(ioDispatcher) {
            val result = characterDao.getCharactersOrderedByName()
            val characters = result.map { it.asDomainModel() }
            return@withContext Result.success(characters)
        }

    override suspend fun getCharactersSortedByRace(): Result<List<Character>> =
        withContext(ioDispatcher) {
            val result = characterDao.getCharactersOrderedByRace()
            val characters = result.map { it.asDomainModel() }
            return@withContext Result.success(characters)
        }

    override suspend fun getCharactersSortedByType(): Result<List<Character>> =
        withContext(ioDispatcher) {
            val result = characterDao.getCharactersOrderedByType()
            val characters = result.map { it.asDomainModel() }
            return@withContext Result.success(characters)
        }

    override suspend fun getCharactersSortedByWorld(): Result<List<Character>> =
        withContext(ioDispatcher) {
            val result = characterDao.getCharactersOrderedByWorld()
            val characters = result.map { it.asDomainModel() }
            return@withContext Result.success(characters)
        }

    override suspend fun getCharactersOfUser(id: String): Result<List<Character>> =
        withContext(ioDispatcher) {
            val result = characterDao.getCharactersOfUser(id)
            val characters = result.map { it.asDomainModel() }
            return@withContext Result.success(characters)
        }

    override suspend fun getCharacter(id: String): Result<Character> = withContext(ioDispatcher) {
        val result = characterDao.getCharacter(id)
        return@withContext Result.success(result.asDomainModel())
    }

    override suspend fun saveCharacter(character: Character): Result<Boolean> =
        withContext(ioDispatcher) {
            characterDao.saveCharacter(character.asDatabaseModel())
            return@withContext Result.success(true)
        }

    override suspend fun deleteCharacter(character: Character): Result<Boolean> =
        withContext(ioDispatcher) {
            characterDao.deleteCharacter(character.asDatabaseModel())
            return@withContext Result.success(true)
        }

    override suspend fun refreshCharacters() = withContext(ioDispatcher) {
        val result = characterService.refreshCharacters()
        if (result.isSuccess) {
            result.getOrNull()?.forEach {
                save(it)
            }
        }
    }

    override suspend fun refreshCharacter(id: String) = withContext(ioDispatcher) {
        val result = characterService.refreshCharacter(id)
        if (result.isSuccess) {
            result.getOrNull()?.let {
                save(it)
            }
        }
    }

    private suspend fun save(characterDTO: CharacterDTO) {
        val character = characterDTO.asDatabaseModel()
        val abilities = characterDTO.abilities.map {
            CharacterAbilityCrossRef(character.id, it)
        }
        val equipments = characterDTO.equipments.map {
            CharacterEquipmentCrossRef(character.id, it)
        }
        val forces = characterDTO.forces.map {
            CharacterForceCrossRef(character.id, it)
        }
        val handicaps = characterDTO.handicaps.map {
            CharacterHandicapCrossRef(character.id, it)
        }
        val talents = characterDTO.talents.map {
            CharacterTalentCrossRef(character.id, it)
        }
        characterDao.saveCharacter(character)
        characterDao.saveAbilities(abilities)
        characterDao.saveEquipments(equipments)
        characterDao.saveForces(forces)
        characterDao.saveHandicaps(handicaps)
        characterDao.saveTalents(talents)
    }
}


fun CharacterDTO.asDatabaseModel(): CharacterEntity {
    return CharacterEntity(
        name,
        race ?: "",
        description,
        charisma,
        constitution,
        deception,
        dexterity,
        intelligence,
        investigation,
        perception,
        stealth,
        strength,
        willpower,
        movement,
        parry,
        toughness,
        typeDTOStringToDatabase(type),
        owner,
        world ?: "",
        id
    )
}

fun Character.asDatabaseModel(): CharacterEntity {
    return CharacterEntity(
        name,
        race?.id ?: "",
        description,
        charisma,
        constitution,
        deception,
        dexterity,
        intelligence,
        investigation,
        perception,
        stealth,
        strength,
        willpower,
        movement,
        parry,
        toughness,
        type?.asDatabaseModel() ?: "",
        owner,
        world?.id ?: "",
        id
    )
}

fun CharacterWithEverything.asDomainModel(): Character {
    return Character(
        character.name,
        race?.asDomainModel(world),
        character.description,
        character.charisma,
        character.constitution,
        character.deception,
        character.dexterity,
        character.intelligence,
        character.investigation,
        character.perception,
        character.stealth,
        character.strength,
        character.willpower,
        character.movement,
        character.parry,
        character.toughness,
        abilities.map { it.asDomainModel(world) }.toMutableList(),
        equipments.map { it.asDomainModel(world) }.toMutableList(),
        forces.map { it.asDomainModel(world) }.toMutableList(),
        handicaps.map { it.asDomainModel(world) }.toMutableList(),
        talents.map { it.asDomainModel(world) }.toMutableList(),
        typeDatabaseStringToCharacterType(character.type),
        character.owner,
        world?.asDomainModel(),
        character.id
    )
}

fun typeDatabaseStringToCharacterType(dtoValue: String?): Character.Type? {
    return when (dtoValue) {
        "Player" -> Character.Type.PLAYER
        "NPC" -> Character.Type.NPC
        "Monster" -> Character.Type.MONSTER
        else -> null
    }
}

fun typeDTOStringToDatabase(dtoValue: String?): String {
    return when (dtoValue) {
        "Spieler" -> "Player"
        "NPC" -> "NPC"
        "Monster" -> "Monster"
        else -> "NPC"
    }
}

fun Character.Type.asDTOValue(): String {
    return when (this) {
        Character.Type.PLAYER -> "Spieler"
        Character.Type.NPC -> "NPC"
        Character.Type.MONSTER -> "Monster"
    }
}

fun Character.Type.asDatabaseModel(): String {
    return when (this) {
        Character.Type.PLAYER -> "Player"
        Character.Type.NPC -> "NPC"
        Character.Type.MONSTER -> "Monster"
    }
}
