package com.cabbagebeyond.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cabbagebeyond.data.local.CharacterRank
import java.util.*

@Entity(tableName = "force")
data class ForceEntity(
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "cost")
    var cost: String,
    @ColumnInfo(name = "duration")
    var duration: String,
    @ColumnInfo(name = "rank")
    var requiredRank: CharacterRank,
    @ColumnInfo(name = "range")
    var range: String,
    @ColumnInfo(name = "shaping")
    var shaping: String,
    @ColumnInfo(name = "world")
    var world: String,
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString()
)