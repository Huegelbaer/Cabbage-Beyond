package com.cabbagebeyond.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "roles")
data class RoleDTO(
    var name: String,
    var features: List<String>,
    @PrimaryKey var id: String = UUID.randomUUID().toString()
)