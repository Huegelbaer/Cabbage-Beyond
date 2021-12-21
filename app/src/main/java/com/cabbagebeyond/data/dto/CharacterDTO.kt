package com.cabbagebeyond.data.dto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "sw_characters",
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
data class CharacterDTO(
    var name: String,
    var description: String,
    var charisma : Int,
    var constitution: String,
    var deception: String,
    var dexterity: String,
    var intelligence : String,
    var investigation : String,
    var perception : String,
    var stealth : String,
    var strength : String,
    var willpower : String,
    var movement : Int,
    var parry : Int,
    var toughness : Int,
    var abilities : List<String>,
    var equipments : List<String>,
    var forces : List<String>,
    var handicaps : List<String>,
    var talents : List<String>,
    var type : String,
    var owner : String,
    var world: String,
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
)
