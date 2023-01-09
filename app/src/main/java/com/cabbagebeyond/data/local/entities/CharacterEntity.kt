package com.cabbagebeyond.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "character")
data class CharacterEntity(
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "race")
    var race: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "charisma")
    var charisma: Int,
    @ColumnInfo(name = "constitution")
    var constitution: String,
    @ColumnInfo(name = "deception")
    var deception: String,
    @ColumnInfo(name = "dexterity")
    var dexterity: String,
    @ColumnInfo(name = "intelligence")
    var intelligence: String,
    @ColumnInfo(name = "investigation")
    var investigation: String,
    @ColumnInfo(name = "perception")
    var perception: String,
    @ColumnInfo(name = "stealth")
    var stealth: String,
    @ColumnInfo(name = "strength")
    var strength: String,
    @ColumnInfo(name = "willpower")
    var willpower: String,
    @ColumnInfo(name = "movement")
    var movement: Int,
    @ColumnInfo(name = "parry")
    var parry: Int,
    @ColumnInfo(name = "toughness")
    var toughness: String = "",
    @ColumnInfo(name = "type")
    var type: String,
    @ColumnInfo(name = "owner")
    var owner: String,
    @ColumnInfo(name = "world")
    var world: String,
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString()
)