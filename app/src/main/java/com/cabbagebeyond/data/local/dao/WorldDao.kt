package com.cabbagebeyond.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cabbagebeyond.data.local.entities.WorldEntity

@Dao
interface WorldDao {

    @Query("SELECT * from world")
    suspend fun getWorlds(): List<WorldEntity>

    @Query("SELECT * from world WHERE id = :id")
    suspend fun getWorld(id: String): WorldEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWorld(world: WorldEntity)

    @Delete
    suspend fun deleteWorld(world: WorldEntity)

}