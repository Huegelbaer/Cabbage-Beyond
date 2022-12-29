package com.cabbagebeyond.data.local.dao

import androidx.room.*
import com.cabbagebeyond.data.local.entities.RaceEntity
import com.cabbagebeyond.data.local.entities.RaceFeatureEntity
import com.cabbagebeyond.data.local.relations.RaceFeatureCrossRef
import com.cabbagebeyond.data.local.relations.RaceWithWorld

@Dao
interface RaceDao {

    @Transaction
    @Query("SELECT * FROM race")
    suspend fun getRaces(): List<RaceWithWorld>

    @Transaction
    @Query("SELECT * FROM race WHERE id = :id")
    suspend fun getRace(id: String): RaceWithWorld

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRace(race: RaceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFeatures(features: List<RaceFeatureEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRaceFeatures(features: List<RaceFeatureCrossRef>)

    @Delete
    suspend fun deleteRace(race: RaceEntity)
}