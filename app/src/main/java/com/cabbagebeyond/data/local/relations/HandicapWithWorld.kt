package com.cabbagebeyond.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.cabbagebeyond.data.local.entities.HandicapEntity
import com.cabbagebeyond.data.local.entities.WorldEntity

data class HandicapWithWorld(
    @Embedded
    val handicap: HandicapEntity,
    @Relation(
        parentColumn = "world",
        entityColumn = "id"
    )
    val world: WorldEntity?
)