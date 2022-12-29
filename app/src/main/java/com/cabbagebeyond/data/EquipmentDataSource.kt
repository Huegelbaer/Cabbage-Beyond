package com.cabbagebeyond.data

import com.cabbagebeyond.model.Equipment

interface EquipmentDataSource {

    //fun observeEquipments(): LiveData<Result<List<Equipment>>>

    //fun observeEquipment(id: String): LiveData<Result<Equipment>>

    suspend fun getEquipments(): Result<List<Equipment>>

    suspend fun getEquipments(ids: List<String>): Result<List<Equipment>>

    suspend fun getEquipment(id: String): Result<Equipment>

    suspend fun saveEquipment(equipment: Equipment): Result<Boolean>

    suspend fun deleteEquipment(equipment: Equipment): Result<Boolean>

    /*
        REMOTE
     */

    suspend fun refreshEquipments()

    suspend fun refreshEquipment(id: String)

}