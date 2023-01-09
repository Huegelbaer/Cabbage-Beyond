package com.cabbagebeyond.data

import com.cabbagebeyond.model.Character

interface CharacterDataSource {
    
    //fun observeCharacters(): LiveData<Result<List<Character>>>

    //fun observeCharacter(id: String): LiveData<Result<Character>>

    suspend fun getCharacters(): Result<List<Character>>

    suspend fun getCharactersSortedByName(): Result<List<Character>>

    suspend fun getCharactersSortedByRace(): Result<List<Character>>

    suspend fun getCharactersSortedByType(): Result<List<Character>>

    suspend fun getCharactersSortedByWorld(): Result<List<Character>>

    suspend fun getCharactersOfUser(id: String): Result<List<Character>>

    suspend fun getCharacter(id: String): Result<Character>

    suspend fun saveCharacter(character: Character): Result<Boolean>

    suspend fun deleteCharacter(character: Character): Result<Boolean>

    /*
        REMOTE
    */

    suspend fun refreshCharacters()

    suspend fun refreshCharacter(id: String)
}