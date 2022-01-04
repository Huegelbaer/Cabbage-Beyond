package com.cabbagebeyond.data

import com.cabbagebeyond.data.dto.CharacterDTO

interface CharacterDataSource {
    
    //fun observeCharacters(): LiveData<Result<List<CharacterDTO>>>

    suspend fun getCharacters(): Result<List<CharacterDTO>>

    //suspend fun refreshCharacters()

    //fun observeCharacter(id: String): LiveData<Result<CharacterDTO>>

    suspend fun getCharacter(id: String): Result<CharacterDTO>

    //suspend fun refreshCharacter(id: String)

    suspend fun saveCharacter(character: CharacterDTO)

    //suspend fun deleteAllCharacters()

    suspend fun deleteCharacter(id: String)
}