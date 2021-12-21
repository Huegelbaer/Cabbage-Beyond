package com.cabbagebeyond.data.dto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "sw_abilities",
    foreignKeys = [
        ForeignKey(
            entity = WorldDTO::class,
            parentColumns = ["id"],
            childColumns = ["world"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class AbilityDTO(
    var name: String,
    var description: String,
    var attribute: String,
    var world: String,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
)