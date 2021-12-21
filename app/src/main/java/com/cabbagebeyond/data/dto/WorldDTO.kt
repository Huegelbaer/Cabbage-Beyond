package com.cabbagebeyond.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "worlds")
data class WorldDTO(
    var name: String,
    var description: String?,
    var rulebook: String,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
)