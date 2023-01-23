package com.cabbagebeyond.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.cabbagebeyond.data.local.entities.EquipmentEntity
import com.cabbagebeyond.data.local.entities.WorldEntity
import com.cabbagebeyond.data.repository.asDomainModel
import com.cabbagebeyond.model.Equipment

data class EquipmentWithWorld(
    @Embedded
    val equipment: EquipmentEntity,
    @Relation(
        parentColumn = "world",
        entityColumn = "id"
    )
    val world: WorldEntity?
)

fun List<EquipmentWithWorld>.asDomainModel(): List<Equipment> {
    return map {
        it.asDomainModel()
    }
}

fun EquipmentWithWorld.asDomainModel(): Equipment {
    return equipment.asDomainModel(world)
}

fun EquipmentEntity.asDomainModel(world: WorldEntity?): Equipment {
    val type = when (type) {
        EquipmentEntity.Type.WEAPON -> Equipment.Type.WEAPON
        EquipmentEntity.Type.ARMOR -> Equipment.Type.ARMOR
        EquipmentEntity.Type.OTHERS -> Equipment.Type.OTHERS
    }
    return Equipment(
        name,
        description,
        cost,
        weight,
        requirements,
        type,
        world?.asDomainModel(),
        id
    )
}