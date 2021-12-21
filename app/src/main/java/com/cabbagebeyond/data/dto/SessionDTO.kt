package com.cabbagebeyond.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "stories",
    foreignKeys = [
        ForeignKey(
            entity = UserDTO::class,
            parentColumns = ["id"],
            childColumns = ["owner"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = StoryDTO::class,
            parentColumns = ["id"],
            childColumns = ["story"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SessionDTO(
    var name: String,
    var description: String,
    var player: String,
    var status: String,
    @ColumnInfo(name = "invited_players")
    var invitedPlayers: List<String>,
    var owner: String,
    var story: String,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
)