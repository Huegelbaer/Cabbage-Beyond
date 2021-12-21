package com.cabbagebeyond.data.dto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "stories",
    foreignKeys = [
        ForeignKey(
            entity = WorldDTO::class,
            parentColumns = ["id"],
            childColumns = ["world"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserDTO::class,
            parentColumns = ["id"],
            childColumns = ["owner"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class StoryDTO(
    var name: String,
    var description: String,
    var story: String,
    var owner: String,
    var world: String,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
)