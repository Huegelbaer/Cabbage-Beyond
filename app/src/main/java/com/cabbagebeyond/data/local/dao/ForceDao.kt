package com.cabbagebeyond.data.local.dao

import androidx.room.*
import com.cabbagebeyond.data.local.entities.ForceEntity
import com.cabbagebeyond.data.local.relations.ForceWithWorld

@Dao
interface ForceDao {

    @Transaction
    @Query("SELECT * FROM force")
    suspend fun getForces(): List<ForceWithWorld>

    @Transaction
    @Query("SELECT * FROM force WHERE id in (:ids)")
    suspend fun getForces(ids: List<String>): List<ForceWithWorld>

    @Transaction
    @Query("SELECT * FROM force WHERE id = :id")
    suspend fun getForce(id: String): ForceWithWorld

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveForce(force: ForceEntity)

    @Delete
    suspend fun deleteForce(force: ForceEntity)
}