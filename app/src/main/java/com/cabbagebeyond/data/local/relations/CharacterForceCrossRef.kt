package com.cabbagebeyond.data.local.relations

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["character_id", "force_id"])
data class CharacterForceCrossRef(
    @ColumnInfo(name = "character_id")
    val character: String,
    @ColumnInfo(name = "force_id")
    val force: String
)