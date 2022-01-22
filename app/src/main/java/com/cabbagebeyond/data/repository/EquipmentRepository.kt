package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.EquipmentDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.data.dao.EquipmentDao
import com.cabbagebeyond.data.dto.EquipmentDTO
import com.cabbagebeyond.model.Equipment
import com.cabbagebeyond.model.World
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EquipmentRepository(
    private val equipmentDao: EquipmentDao,
    private val worldDataSource: WorldDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : EquipmentDataSource {

    override suspend fun getEquipments(): Result<List<Equipment>> = withContext(ioDispatcher) {
        val equipments = equipmentDao.getEquipments()
        return@withContext mapList(equipments)
    }

    override suspend fun getEquipments(ids: List<String>): Result<List<Equipment>> = withContext(ioDispatcher) {
        val equipments = equipmentDao.getEquipments(ids)
        return@withContext mapList(equipments)
    }

    override suspend fun getEquipment(id: String): Result<Equipment> = withContext(ioDispatcher) {
        val equipments = equipmentDao.getEquipment(id)
        return@withContext map(equipments)
    }

    override suspend fun saveEquipment(equipment: Equipment): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext equipmentDao.saveEquipment(equipment.asDatabaseModel())
    }

    override suspend fun deleteEquipment(id: String): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext equipmentDao.deleteEquipment(id)
    }

    private suspend fun mapList(result: Result<List<EquipmentDTO>>): Result<List<Equipment>> {
        val worlds = worldDataSource.getWorlds().getOrDefault(listOf())
        return result.mapCatching {
            it.asDomainModel(worlds)
        }
    }

    private suspend fun map(result: Result<EquipmentDTO>): Result<Equipment> {
        return result.mapCatching {
            val world = worldDataSource.getWorld(it.world).getOrNull()
            it.asDomainModel(world)
        }
    }
}

fun List<EquipmentDTO>.asDomainModel(worlds: List<World>): List<Equipment> {
    return map { equipment ->
        val world = worlds.firstOrNull { it.id == equipment.world }
        equipment.asDomainModel(world)
    }
}

fun EquipmentDTO.asDomainModel(world: World?): Equipment {
    return Equipment(name, description, cost, weight, requirements.split(", "), type, world, id)
}

fun List<Equipment>.asDatabaseModel(): List<EquipmentDTO> {
    return map {
        it.asDatabaseModel()
    }
}

fun Equipment.asDatabaseModel(): EquipmentDTO {
    return EquipmentDTO(name, description, cost, weight, requirements.joinToString(), type, world?.id ?: "", id)
}