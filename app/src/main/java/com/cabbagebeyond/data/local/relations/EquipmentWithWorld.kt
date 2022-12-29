package com.cabbagebeyond.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.cabbagebeyond.data.local.entities.EquipmentEntity
import com.cabbagebeyond.data.local.entities.WorldEntity
import com.cabbagebeyond.data.local.entities.asDomainModel
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
    val type = when (equipment.type) {
        EquipmentEntity.Type.WEAPON -> Equipment.Type.WEAPON
        EquipmentEntity.Type.ARMOR -> Equipment.Type.ARMOR
        EquipmentEntity.Type.OTHERS -> Equipment.Type.OTHERS
    }
    return Equipment(
        equipment.name,
        equipment.description,
        equipment.cost,
        equipment.weight,
        equipment.requirements,
        type,
        world?.asDomainModel(),
        equipment.id
    )
}