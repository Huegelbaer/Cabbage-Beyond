package com.cabbagebeyond.data.local.dao

import androidx.room.*
import com.cabbagebeyond.data.local.entities.HandicapEntity
import com.cabbagebeyond.data.local.relations.HandicapWithWorld

@Dao
interface HandicapDao {

    @Query("SELECT * FROM handicap")
    suspend fun getHandicaps(): List<HandicapWithWorld>

    @Query("SELECT * FROM handicap WHERE id in (:ids)")
    suspend fun getHandicaps(ids: List<String>): List<HandicapWithWorld>

    @Query("SELECT * FROM handicap WHERE id = :id")
    suspend fun getHandicap(id: String): HandicapWithWorld

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveHandicap(handicap: HandicapEntity)

    @Delete
    suspend fun deleteHandicap(handicap: HandicapEntity)
}