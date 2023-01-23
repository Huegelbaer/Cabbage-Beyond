package com.cabbagebeyond.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "handicap")
data class HandicapEntity(
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "type")
    var type: Type,
    @ColumnInfo(name = "world")
    var world: String,
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString()
) {
    enum class Type {
        SLIGHT,
        SLIGHT_OR_HEAVY,
        HEAVY,
    }
}
