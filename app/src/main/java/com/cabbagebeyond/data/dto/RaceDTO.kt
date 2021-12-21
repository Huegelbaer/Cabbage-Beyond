package com.cabbagebeyond.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "sw_races",
    foreignKeys = [
        ForeignKey(
            entity = WorldDTO::class,
            parentColumns = ["id"],
            childColumns = ["world"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class RaceDTO(
    var name: String,
    var description: String,
    @ColumnInfo(name = "race_features")
    var raceFeatures: List<String>,
    var world: String,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
)
