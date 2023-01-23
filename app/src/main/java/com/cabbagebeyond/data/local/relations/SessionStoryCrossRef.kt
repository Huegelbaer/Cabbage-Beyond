package com.cabbagebeyond.data.local.relations

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["session_id", "story_id"])
data class SessionStoryCrossRef(
    @ColumnInfo(name = "session_id")
    val session: String,
    @ColumnInfo(name = "story_id")
    val story: String,
)