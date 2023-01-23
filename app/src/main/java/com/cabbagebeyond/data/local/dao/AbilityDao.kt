package com.cabbagebeyond.data.local.dao

import androidx.room.*
import com.cabbagebeyond.data.local.entities.AbilityEntity
import com.cabbagebeyond.data.local.relations.AbilityWithWorld

@Dao
interface AbilityDao {

    @Transaction
    @Query("SELECT * FROM ability")
    suspend fun getAbilities(): List<AbilityWithWorld>

    @Transaction
    @Query("SELECT * FROM ability WHERE id in (:ids)")
    suspend fun getAbilities(ids: List<String>): List<AbilityWithWorld>

    @Transaction
    @Query("SELECT * FROM ability WHERE id = :id")
    suspend fun getAbility(id: String): AbilityWithWorld

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAbility(ability: AbilityEntity)

    @Delete
    suspend fun deleteAbility(ability: AbilityEntity)
}