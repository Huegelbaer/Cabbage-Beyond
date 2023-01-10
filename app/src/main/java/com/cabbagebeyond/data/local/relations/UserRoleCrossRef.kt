package com.cabbagebeyond.data.local.relations

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["user_id", "role_id"])
data class UserRoleCrossRef(
    @ColumnInfo(name = "user_id")
    val user: String,
    @ColumnInfo(name = "role_id")
    val role: String
)