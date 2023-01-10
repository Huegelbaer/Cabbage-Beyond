package com.cabbagebeyond.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cabbagebeyond.data.local.entities.RoleEntity
import com.cabbagebeyond.data.local.entities.UserEntity

data class UserWithRoles(
    @Embedded
    val user: UserEntity,
    @Relation(
        entity = RoleEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = UserRoleCrossRef::class,
            parentColumn = "user_id",
            entityColumn = "role_id"
        )
    )
    val roles: List<RoleEntity>,
)