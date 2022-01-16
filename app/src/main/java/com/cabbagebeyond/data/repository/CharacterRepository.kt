package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.CharacterDataSource
import com.cabbagebeyond.model.Character
import com.cabbagebeyond.data.dao.CharacterDao
import com.cabbagebeyond.data.dto.asDatabaseModel
import com.cabbagebeyond.data.dto.asDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterRepository(
    private val characterDao: CharacterDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): CharacterDataSource {

    override suspend fun getCharacters(): Result<List<Character>> = withContext(ioDispatcher) {
        return@withContext characterDao.getCharacters().mapCatching { it.asDomainModel() }
    }

    override suspend fun getCharactersSortedByName(): Result<List<Character>> = withContext(ioDispatcher) {
        return@withContext characterDao.getCharactersOrderedByName().mapCatching { it.asDomainModel() }
    }

    override suspend fun getCharactersSortedByRace(): Result<List<Character>> = withContext(ioDispatcher) {
        return@withContext characterDao.getCharactersOrderedByRace().mapCatching { it.asDomainModel() }
    }

    override suspend fun getCharactersSortedByType(): Result<List<Character>> = withContext(ioDispatcher) {
        return@withContext characterDao.getCharactersOrderedByType().mapCatching { it.asDomainModel() }
    }

    override suspend fun getCharactersSortedByWorld(): Result<List<Character>> = withContext(ioDispatcher) {
        return@withContext characterDao.getCharactersOrderedByWorld().mapCatching { it.asDomainModel() }
    }

    override suspend fun getCharactersOfUser(id: String): Result<List<Character>> = withContext(ioDispatcher) {
        return@withContext characterDao.getCharactersOfUser(id).mapCatching { it.asDomainModel() }
    }

    override suspend fun getCharacter(id: String): Result<Character> = withContext(ioDispatcher) {
        return@withContext characterDao.getCharacter(id).mapCatching { it.asDomainModel() }
    }

    override suspend fun saveCharacter(character: Character) = withContext(ioDispatcher) {
        characterDao.saveCharacter(character.asDatabaseModel())
    }

    override suspend fun deleteCharacter(id: String) = withContext(ioDispatcher) {
        characterDao.deleteCharacter(id)
    }

}