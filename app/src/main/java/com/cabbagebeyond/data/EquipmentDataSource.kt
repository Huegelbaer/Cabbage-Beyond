package com.cabbagebeyond.data

import androidx.lifecycle.LiveData
import com.cabbagebeyond.data.dto.EquipmentDTO

interface EquipmentDataSource {

    fun observeEquipments(): LiveData<Result<List<EquipmentDTO>>>

    suspend fun getEquipments(): Result<List<EquipmentDTO>>

    suspend fun refreshEquipments()

    fun observeEquipment(id: String): LiveData<Result<EquipmentDTO>>

    suspend fun getEquipment(id: String): Result<EquipmentDTO>

    suspend fun refreshEquipment(id: String)

    suspend fun saveEquipment(world: EquipmentDTO)

    suspend fun deleteAllEquipments()

    suspend fun deleteEquipment(id: String)
}