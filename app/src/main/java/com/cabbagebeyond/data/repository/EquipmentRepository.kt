package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.EquipmentDataSource
import com.cabbagebeyond.data.dao.EquipmentDao
import com.cabbagebeyond.data.dto.asDatabaseModel
import com.cabbagebeyond.data.dto.asDomainModel
import com.cabbagebeyond.model.Equipment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EquipmentRepository(
    private val equipmentDao: EquipmentDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : EquipmentDataSource {

    override suspend fun getEquipments(): Result<List<Equipment>> = withContext(ioDispatcher) {
        return@withContext equipmentDao.getEquipments().mapCatching { it.asDomainModel() }
    }

    override suspend fun getEquipment(id: String): Result<Equipment> = withContext(ioDispatcher) {
        return@withContext equipmentDao.getEquipment(id).mapCatching { it.asDomainModel() }
    }

    override suspend fun saveEquipment(equipment: Equipment) = withContext(ioDispatcher) {
        equipmentDao.saveEquipment(equipment.asDatabaseModel())
    }

    override suspend fun deleteEquipment(id: String) = withContext(ioDispatcher) {
        equipmentDao.deleteEquipment(id)
    }

}