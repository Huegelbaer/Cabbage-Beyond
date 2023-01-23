package com.cabbagebeyond.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.cabbagebeyond.data.local.entities.ForceEntity
import com.cabbagebeyond.data.local.entities.WorldEntity

data class ForceWithWorld(
    @Embedded
    val force: ForceEntity,
    @Relation(
        parentColumn = "world",
        entityColumn = "id"
    )
    val world: WorldEntity?
)