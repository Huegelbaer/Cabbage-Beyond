package com.cabbagebeyond.data.local

import com.cabbagebeyond.data.CharacterDataSource
import com.cabbagebeyond.data.dto.CharacterDTO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterRepository(
    private val characterDao: CharacterDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): CharacterDataSource {

    override suspend fun getCharacters(): Result<List<CharacterDTO>> = withContext(ioDispatcher) {
        return@withContext characterDao.getCharacters()
    }

    override suspend fun getCharacter(id: String): Result<CharacterDTO> = withContext(ioDispatcher) {
        return@withContext characterDao.getCharacter(id)
    }

    override suspend fun saveCharacter(character: CharacterDTO) = withContext(ioDispatcher) {
        characterDao.saveCharacter(character)
    }

    override suspend fun deleteCharacter(id: String) = withContext(ioDispatcher) {
        characterDao.deleteCharacter(id)
    }

}