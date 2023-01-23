package com.cabbagebeyond.data.local.relations

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["story_id", "owner_id"])
data class StoryOwnerCrossRef(
    @ColumnInfo(name = "story_id")
    val story: String,
    @ColumnInfo(name = "owner_id")
    val owner: String,
)