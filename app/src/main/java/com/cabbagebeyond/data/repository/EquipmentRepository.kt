package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.EquipmentDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.data.local.dao.EquipmentDao
import com.cabbagebeyond.data.dto.EquipmentDTO
import com.cabbagebeyond.data.local.entities.EquipmentEntity
import com.cabbagebeyond.data.local.relations.asDomainModel
import com.cabbagebeyond.data.remote.EquipmentService
import com.cabbagebeyond.model.Equipment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EquipmentRepository(
    private val equipmentDao: EquipmentDao,
    private val equipmentService: EquipmentService,
    private val worldDataSource: WorldDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : EquipmentDataSource {

    override suspend fun getEquipments(): Result<List<Equipment>> = withContext(ioDispatcher) {
        val result = equipmentDao.getEquipments()
        val list = result.map { it.asDomainModel() }
        return@withContext Result.success(list)
    }

    override suspend fun getEquipments(ids: List<String>): Result<List<Equipment>> = withContext(ioDispatcher) {
        val result = equipmentDao.getEquipments(ids)
        val list = result.map { it.asDomainModel() }
        return@withContext Result.success(list)
    }

    override suspend fun getEquipment(id: String): Result<Equipment> = withContext(ioDispatcher) {
        val result = equipmentDao.getEquipment(id)
        val list = result.asDomainModel()
        return@withContext Result.success(list)
    }

    override suspend fun saveEquipment(equipment: Equipment) = withContext(ioDispatcher) {
        equipmentDao.saveEquipment(equipment.asDatabaseModel())
        return@withContext Result.success(true)
    }

    override suspend fun deleteEquipment(equipment: Equipment) = withContext(ioDispatcher) {
        equipmentDao.deleteEquipment(equipment.asDatabaseModel())
        return@withContext Result.success(true)
    }

    override suspend fun refreshEquipments() = withContext(ioDispatcher) {
        val result = equipmentService.refreshEquipments()
        if (result.isSuccess) {
            result.getOrNull()?.forEach {
                equipmentDao.saveEquipment(it.asDatabaseModel())
            }
        }
    }

    override suspend fun refreshEquipment(id: String) = withContext(ioDispatcher) {
        val result = equipmentService.refreshEquipment(id)
        if (result.isSuccess) {
            result.getOrNull()?.let {
                equipmentDao.saveEquipment(it.asDatabaseModel())
            }
        }
    }
}

private fun EquipmentDTO.asDatabaseModel(): EquipmentEntity {
    return EquipmentEntity(name, description, cost, weight, requirements.split(", "), valueToEquipmentType(type), world, id)
}

private fun List<Equipment>.asDatabaseModel(): List<EquipmentEntity> {
    return map {
        it.asDatabaseModel()
    }
}

private fun Equipment.asDatabaseModel(): EquipmentEntity {
    return EquipmentEntity(name, description, cost, weight, requirements, type?.asDatabaseModel() ?: EquipmentEntity.Type.OTHERS, world?.id ?: "", id)
}

private fun valueToEquipmentType(dtoValue: String?): EquipmentEntity.Type {
    return when(dtoValue) {
        "Waffe" -> EquipmentEntity.Type.WEAPON
        "Rüstung" -> EquipmentEntity.Type.ARMOR
        "Sonstiges" -> EquipmentEntity.Type.OTHERS
        else -> EquipmentEntity.Type.OTHERS
    }
}

private fun Equipment.Type.asDatabaseModel(): EquipmentEntity.Type {
    return when(this) {
        Equipment.Type.WEAPON -> EquipmentEntity.Type.WEAPON
        Equipment.Type.ARMOR -> EquipmentEntity.Type.ARMOR
        Equipment.Type.OTHERS -> EquipmentEntity.Type.OTHERS
    }
}