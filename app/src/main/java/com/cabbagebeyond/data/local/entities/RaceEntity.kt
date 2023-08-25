package com.cabbagebeyond.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "race")
data class RaceEntity(
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "world")
    var world: String,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id", index = true)
    val id: String = UUID.randomUUID().toString()
)