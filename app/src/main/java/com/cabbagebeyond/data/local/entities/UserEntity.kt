package com.cabbagebeyond.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "user")
data class UserEntity(
    @ColumnInfo(name = "username")
    var username: String = "",
    @ColumnInfo(name = "email")
    var email: String = "",
    @ColumnInfo(name = "features")
    var features: List<String> = listOf(),
    @ColumnInfo(name = "roles")
    var roleIds: List<String> = listOf(),
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString()
)