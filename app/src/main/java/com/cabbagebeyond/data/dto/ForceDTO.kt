package com.cabbagebeyond.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "sw_forces",
    foreignKeys = [
        ForeignKey(
            entity = WorldDTO::class,
            parentColumns = ["id"],
            childColumns = ["world"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class ForceDTO(
    var name: String,
    var description: String,
    var cost: String,
    var duration: String,
    @ColumnInfo(name = "rang_requirement")
    var rangRequirement: String,
    var range: String,
    var shaping: String,
    var world: String,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
)
