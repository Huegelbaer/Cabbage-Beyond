package com.cabbagebeyond.data.local.relations

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["character_id", "equipment_id"])
data class CharacterEquipmentCrossRef(
    @ColumnInfo(name = "character_id")
    val character: String,
    @ColumnInfo(name = "equipment_id")
    val equipment: String
)