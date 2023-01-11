package com.cabbagebeyond.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "story")
data class StoryEntity(
    @ColumnInfo(name = "title")
    var name: String = "",
    @ColumnInfo(name = "description")
    var description: String = "",
    @ColumnInfo(name = "story")
    var story: String = "",
    @ColumnInfo(name = "owner")
    var owner: String = "",
    @ColumnInfo(name = "world")
    var world: String = "",
    @ColumnInfo(name = "rule_set")
    var rulebook: String = "",
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString()
)
