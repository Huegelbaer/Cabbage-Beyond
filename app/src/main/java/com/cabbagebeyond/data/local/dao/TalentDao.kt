package com.cabbagebeyond.data.local.dao

import androidx.room.*
import com.cabbagebeyond.data.local.entities.TalentEntity
import com.cabbagebeyond.data.local.relations.TalentWithWorld

@Dao
interface TalentDao {

    @Transaction
    @Query("SELECT * FROM talent")
    suspend fun getTalents(): List<TalentWithWorld>

    @Transaction
    @Query("SELECT * FROM talent WHERE id in (:ids)")
    suspend fun getTalents(ids: List<String>): List<TalentWithWorld>

    @Transaction
    @Query("SELECT * FROM talent WHERE id = :id")
    suspend fun getTalent(id: String): TalentWithWorld

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTalent(talent: TalentEntity)

    @Delete
    suspend fun deleteTalent(talent: TalentEntity)
}