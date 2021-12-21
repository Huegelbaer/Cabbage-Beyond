package com.cabbagebeyond.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "users")
data class UserDTO(
    var name: String,
    var email: String,
    var features: List<String>,
    var roles: List<String>,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
)