package com.cabbagebeyond.data.local.relations

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["character_id", "handicap_id"])
data class CharacterHandicapCrossRef(
    @ColumnInfo(name = "character_id")
    val character: String,
    @ColumnInfo(name = "handicap_id")
    val handicap: String
)