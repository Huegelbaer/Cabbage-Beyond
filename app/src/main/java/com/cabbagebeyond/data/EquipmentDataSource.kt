package com.cabbagebeyond.data

import com.cabbagebeyond.model.Equipment

interface EquipmentDataSource {

    //fun observeEquipments(): LiveData<Result<List<Equipment>>>

    suspend fun getEquipments(): Result<List<Equipment>>

    suspend fun getEquipments(ids: List<String>): Result<List<Equipment>>

    //suspend fun refreshEquipments()

    //fun observeEquipment(id: String): LiveData<Result<Equipment>>

    suspend fun getEquipment(id: String): Result<Equipment>

    //suspend fun refreshEquipment(id: String)

    suspend fun saveEquipment(equipment: Equipment): Result<Boolean>

    //suspend fun deleteAllEquipments()

    suspend fun deleteEquipment(id: String): Result<Boolean>
}