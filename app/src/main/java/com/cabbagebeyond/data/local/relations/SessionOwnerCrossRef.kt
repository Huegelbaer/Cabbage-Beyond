package com.cabbagebeyond.data.local.relations

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["session_id", "owner_id"])
data class SessionOwnerCrossRef(
    @ColumnInfo(name = "session_id")
    val session: String,
    @ColumnInfo(name = "owner_id")
    val owner: String,
)