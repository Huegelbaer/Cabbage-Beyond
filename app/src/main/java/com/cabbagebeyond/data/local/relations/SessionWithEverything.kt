package com.cabbagebeyond.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cabbagebeyond.data.local.entities.SessionEntity
import com.cabbagebeyond.data.local.entities.StoryEntity
import com.cabbagebeyond.data.local.entities.UserEntity

data class SessionWithEverything(
    @Embedded
    val session: SessionEntity,
    @Relation(
        entity = StoryEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = SessionStoryCrossRef::class,
            parentColumn = "session_id",
            entityColumn = "story_id"
        )
    )
    val story: StoryEntity,
    @Relation(
        entity = UserEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = SessionOwnerCrossRef::class,
            parentColumn = "session_id",
            entityColumn = "owner_id"
        )
    )
    val owner: UserEntity,
    @Relation(
        entity = UserEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = SessionPlayersCrossRef::class,
            parentColumn = "session_id",
            entityColumn = "player_id"
        )
    )
    val players: List<UserEntity>,
)