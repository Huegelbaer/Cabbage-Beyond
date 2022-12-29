package com.cabbagebeyond.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.cabbagebeyond.data.local.entities.WorldEntity
import com.cabbagebeyond.data.local.entities.RaceEntity

data class RaceWithWorld(
    @Embedded
    val race: RaceEntity,
    @Relation(
        parentColumn = "world",
        entityColumn = "id"
    )
    val world: WorldEntity?
    ?
)