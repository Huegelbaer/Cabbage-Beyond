package com.cabbagebeyond.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cabbagebeyond.data.local.entities.StoryEntity
import com.cabbagebeyond.data.local.entities.UserEntity
import com.cabbagebeyond.data.local.entities.WorldEntity

data class StoryWithEverything(
    @Embedded
    val story: StoryEntity,
    @Relation(
        entity = WorldEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = StoryWorldCrossRef::class,
            parentColumn = "story_id",
            entityColumn = "world_id"
        )
    )
    val world: WorldEntity,
    @Relation(
        entity = UserEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = StoryOwnerCrossRef::class,
            parentColumn = "story_id",
            entityColumn = "owner_id"
        )
    )
    val owner: UserEntity,
)