package com.cabbagebeyond.data.local.dao

import androidx.room.*
import com.cabbagebeyond.data.local.entities.CharacterEntity
import com.cabbagebeyond.data.local.relations.*

@Dao
interface CharacterDao {

    @Transaction
    @Query("SELECT * FROM character")
    suspend fun getCharacters(): List<CharacterWithEverything>

    @Transaction
    @Query("SELECT * FROM character ORDER BY name")
    suspend fun getCharactersOrderedByName(): List<CharacterWithEverything>

    @Transaction
    @Query("SELECT * FROM character ORDER BY race")
    suspend fun getCharactersOrderedByRace(): List<CharacterWithEverything>

    @Transaction
    @Query("SELECT * FROM character ORDER BY type")
    suspend fun getCharactersOrderedByType(): List<CharacterWithEverything>

    @Transaction
    @Query("SELECT * FROM character ORDER BY world")
    suspend fun getCharactersOrderedByWorld(): List<CharacterWithEverything>

    @Transaction
    @Query("SELECT * FROM character WHERE owner = :user")
    suspend fun getCharactersOfUser(user: String): List<CharacterWithEverything>

    @Transaction
    @Query("SELECT * FROM character WHERE id = :id")
    suspend fun getCharacter(id: String): CharacterWithEverything

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCharacter(character: CharacterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAbilities(abilities: List<CharacterAbilityCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEquipments(equipments: List<CharacterEquipmentCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveForces(forces: List<CharacterForceCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveHandicaps(handicaps: List<CharacterHandicapCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTalents(talents: List<CharacterTalentCrossRef>)

    @Delete
    suspend fun deleteCharacter(character: CharacterEntity)
}