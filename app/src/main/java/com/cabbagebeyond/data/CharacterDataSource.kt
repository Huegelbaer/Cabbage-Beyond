package com.cabbagebeyond.data

import com.cabbagebeyond.model.Character

interface CharacterDataSource {
    
    //fun observeCharacters(): LiveData<Result<List<Character>>>

    suspend fun getCharacters(): Result<List<Character>>

    //suspend fun refreshCharacters()

    //fun observeCharacter(id: String): LiveData<Result<Character>>

    suspend fun getCharacter(id: String): Result<Character>

    //suspend fun refreshCharacter(id: String)

    suspend fun saveCharacter(character: Character)

    //suspend fun deleteAllCharacters()

    suspend fun deleteCharacter(id: String)
}