package com.cabbagebeyond.data.local.relations

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["race_id", "feature_id"])
data class RaceFeatureCrossRef(
    @ColumnInfo(name = "race_id")
    val race: String,
    @ColumnInfo(name = "feature_id")
    val feature: String
)