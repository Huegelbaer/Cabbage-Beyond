package com.cabbagebeyond.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cabbagebeyond.data.local.CharacterRank
import java.util.*

@Entity(tableName = "talent")
data class TalentEntity(
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "rank")
    var requiredRank: CharacterRank,
    @ColumnInfo(name = "requirements")
    var requirements: List<String>,
    @ColumnInfo(name = "type")
    var type: Type,
    @ColumnInfo(name = "world")
    var world: String,
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString()
) {
    enum class Type {
        BACKGROUND,
        LEADER,
        EXPERT,
        FIGHT,
        FORCE,
        RACE,
        SOCIAL,
        SUPERNATURAL,
        WILDCARD,
        LEGENDARY,
        DRAGON,
        ATHLETIC,
        PERCEPTION,
        ILLUSION,
        PROVOKE,
        THIEVERY,
        STEALTH,
        TERRITORY,
        INTELLECT,
        STRENGTH,
        UNKNOWN
    }
}