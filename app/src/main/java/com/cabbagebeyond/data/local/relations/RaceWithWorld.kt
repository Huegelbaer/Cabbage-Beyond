package com.cabbagebeyond.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cabbagebeyond.data.local.entities.RaceEntity
import com.cabbagebeyond.data.local.entities.RaceFeatureEntity
import com.cabbagebeyond.data.local.entities.WorldEntity

data class RaceWithWorld(
    @Embedded
    val race: RaceEntity,
    @Relation(
        entity = RaceFeatureEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = RaceFeatureCrossRef::class,
            parentColumn = "race_id",
            entityColumn = "feature_id"
        )
    )
    val features: List<RaceFeatureEntity>,
    @Relation(
        parentColumn = "world",
        entityColumn = "id"
    )
    val world: WorldEntity?
)