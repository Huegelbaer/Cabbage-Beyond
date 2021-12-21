package com.cabbagebeyond.data.dto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "sw_talents",
    foreignKeys = [
        ForeignKey(
            entity = WorldDTO::class,
            parentColumns = ["id"],
            childColumns = ["world"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class HandicapDTO(
    var name: String,
    var description: String,
    var type: String,
    var world: String,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
)
