package com.cabbagebeyond.data.local.relations

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["session_id", "player_id"])
data class SessionPlayersCrossRef(
    @ColumnInfo(name = "session_id")
    val session: String,
    @ColumnInfo(name = "player_id")
    val player: String,
)