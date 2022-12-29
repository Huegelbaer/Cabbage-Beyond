package com.cabbagebeyond.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.cabbagebeyond.data.local.entities.TalentEntity
import com.cabbagebeyond.data.local.entities.WorldEntity

data class TalentWithWorld(
    @Embedded
    val talent: TalentEntity,
    @Relation(
        parentColumn = "world",
        entityColumn = "id"
    )
    val world: WorldEntity?
)