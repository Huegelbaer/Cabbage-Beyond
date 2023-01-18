package com.cabbagebeyond.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "session")
data class SessionEntity(
    @ColumnInfo(name = "title")
    var name: String = "",
    @ColumnInfo(name = "description")
    var description: String = "",
    @ColumnInfo(name = "player")
    var player: String = "",
    @ColumnInfo(name = "status")
    var status: String = "",
    @ColumnInfo(name = "invited_players")
    var invitedPlayers: List<String> = listOf(),
    @ColumnInfo(name = "owner")
    var owner: String = "",
    @ColumnInfo(name = "story")
    var story: String = "",
    @ColumnInfo(name = "rule_set")
    var rulebook: String = "",
    @PrimaryKey(autoGenerate = false)
    val id: String= UUID.randomUUID().toString()
)