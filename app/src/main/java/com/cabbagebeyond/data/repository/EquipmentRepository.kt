package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.EquipmentDataSource
import com.cabbagebeyond.data.dto.EquipmentDTO
import com.cabbagebeyond.data.dao.EquipmentDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EquipmentRepository(
    private val equipmentDao: EquipmentDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : EquipmentDataSource {

    override suspend fun getEquipments(): Result<List<EquipmentDTO>> = withContext(ioDispatcher) {
        return@withContext equipmentDao.getEquipments()
    }

    override suspend fun getEquipment(id: String): Result<EquipmentDTO> = withContext(ioDispatcher) {
        return@withContext equipmentDao.getEquipment(id)
    }

    override suspend fun saveEquipment(equipment: EquipmentDTO) = withContext(ioDispatcher) {
        equipmentDao.saveEquipment(equipment)
    }

    override suspend fun deleteEquipment(id: String) = withContext(ioDispatcher) {
        equipmentDao.deleteEquipment(id)
    }

}