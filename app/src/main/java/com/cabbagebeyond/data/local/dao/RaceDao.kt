package com.cabbagebeyond.data.local.dao

import androidx.room.*
import com.cabbagebeyond.data.local.entities.RaceEntity
import com.cabbagebeyond.data.local.relations.RaceWithWorld

@Dao
interface RaceDao {

    @Query("SELECT * FROM race")
    suspend fun getRaces(): List<RaceWithWorld>

    @Query("SELECT * FROM race WHERE id = :id")
    suspend fun getRace(id: String): RaceWithWorld

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRace(race: RaceEntity)

    @Delete
    suspend fun deleteRace(race: RaceEntity)
}