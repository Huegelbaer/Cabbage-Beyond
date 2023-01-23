package com.cabbagebeyond.data.local.relations

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["story_id", "world_id"])
data class StoryWorldCrossRef(
    @ColumnInfo(name = "story_id")
    val story: String,
    @ColumnInfo(name = "world_id")
    val world: String,
)