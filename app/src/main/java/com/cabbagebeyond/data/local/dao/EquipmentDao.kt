package com.cabbagebeyond.data.local.dao

import androidx.room.*
import com.cabbagebeyond.data.local.entities.EquipmentEntity
import com.cabbagebeyond.data.local.relations.EquipmentWithWorld

@Dao
interface EquipmentDao {

    @Query("SELECT * FROM equipment")
    suspend fun getEquipments(): List<EquipmentWithWorld>

    @Query("SELECT * FROM equipment WHERE id in (:ids)")
    suspend fun getEquipments(ids: List<String>): List<EquipmentWithWorld>

    @Query("SELECT * FROM equipment WHERE id = :id")
    suspend fun getEquipment(id: String): EquipmentWithWorld

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEquipment(equipment: EquipmentEntity)

    @Delete
    suspend fun deleteEquipment(equipment: EquipmentEntity)
}