package com.cabbagebeyond.data.local.relations


import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["character_id", "talent_id"])
data class CharacterTalentCrossRef(
    @ColumnInfo(name = "character_id")
    val character: String,
    @ColumnInfo(name = "talent_id")
    val talent: String
)