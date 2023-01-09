package com.cabbagebeyond.data.local.relations

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["character_id", "ability_id"])
data class CharacterAbilityCrossRef(
    @ColumnInfo(name = "character_id")
    val character: String,
    @ColumnInfo(name = "ability_id")
    val ability: String
)