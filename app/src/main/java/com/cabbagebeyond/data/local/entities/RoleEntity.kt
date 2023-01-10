package com.cabbagebeyond.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "role")
data class RoleEntity(
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "features")
    var features: List<String> = listOf(),
    @PrimaryKey(autoGenerate = false)
    var id: String = UUID.randomUUID().toString()
)