package com.cabbagebeyond.data.dto

import com.cabbagebeyond.model.Character
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.util.*
import kotlin.collections.HashMap

data class CharacterDTO(
    @PropertyName(FIELD_NAME)
    var name: String = "",
    @PropertyName(FIELD_DESCRIPTION)
    var description: String = "",
    @PropertyName(FIELD_CHARISMA)
    var charisma : Int = 0,
    @PropertyName(FIELD_CONSTITUTION)
    var constitution: String = "",
    @PropertyName(FIELD_DECEPTION)
    var deception: String = "",
    @PropertyName(FIELD_DEXTERITY)
    var dexterity: String = "",
    @PropertyName(FIELD_INTELLIGENCE)
    var intelligence : String = "",
    @PropertyName(FIELD_INVESTIGATION)
    var investigation : String = "",
    @PropertyName(FIELD_PERCEPTION)
    var perception : String = "",
    @PropertyName(FIELD_STEALTH)
    var stealth : String = "",
    @PropertyName(FIELD_STRENGTH)
    var strength : String = "",
    @PropertyName(FIELD_WILLPOWER)
    var willpower : String = "",
    @PropertyName(FIELD_MOVEMENT)
    var movement : Int = 0,
    @PropertyName(FIELD_PARRY)
    var parry : Int = 0,
    @PropertyName(FIELD_TOUGHNESS)
    var toughness : String = "",
    @PropertyName(FIELD_ABILITIES)
    var abilities : List<String> = listOf(),
    @PropertyName(FIELD_EQUIPMENTS)
    var equipments : List<String> = listOf(),
    @PropertyName(FIELD_FORCES)
    var forces : List<String> = listOf(),
    @PropertyName(FIELD_HANDICAPS)
    var handicaps : List<String> = listOf(),
    @PropertyName(FIELD_TALENTS)
    var talents : List<String> = listOf(),
    @PropertyName(FIELD_TYPE)
    var type : String = "",
    @PropertyName(FIELD_OWNER)
    var owner : String = "",
    @PropertyName(FIELD_WORLD)
    var world: String = "",
    @DocumentId
    val id: String = UUID.randomUUID().toString()
) {

    companion object {
        const val COLLECTION_TITLE = "sw_characters"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_CHARISMA = "charisma"
        const val FIELD_CONSTITUTION = "constitution"
        const val FIELD_DECEPTION = "deception"
        const val FIELD_DEXTERITY = "dexterity"
        const val FIELD_INTELLIGENCE = "intelligence"
        const val FIELD_INVESTIGATION = "investigation"
        const val FIELD_PERCEPTION = "perception"
        const val FIELD_STEALTH = "stealth"
        const val FIELD_STRENGTH = "strength"
        const val FIELD_WILLPOWER = "willpower"
        const val FIELD_MOVEMENT = "movement"
        const val FIELD_PARRY = "parry"
        const val FIELD_TOUGHNESS = "toughness"
        const val FIELD_ABILITIES = "abilities_ids"
        const val FIELD_EQUIPMENTS = "equipments_ids"
        const val FIELD_FORCES = "forces_ids"
        const val FIELD_HANDICAPS = "handicaps_ids"
        const val FIELD_TALENTS = "talents_ids"
        const val FIELD_TYPE = "type"
        const val FIELD_OWNER = "owner"
        const val FIELD_WORLD = "world_id"
    }

    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            FIELD_NAME to name,
            FIELD_DESCRIPTION to description,
            FIELD_CHARISMA to charisma,
            FIELD_CONSTITUTION to constitution,
            FIELD_DECEPTION to deception,
            FIELD_DEXTERITY to dexterity,
            FIELD_INTELLIGENCE to intelligence,
            FIELD_INVESTIGATION to investigation,
            FIELD_PERCEPTION to perception,
            FIELD_STEALTH to stealth,
            FIELD_STRENGTH to strength,
            FIELD_WILLPOWER to willpower,
            FIELD_MOVEMENT to movement,
            FIELD_PARRY to parry,
            FIELD_TOUGHNESS to toughness,
            FIELD_ABILITIES to abilities,
            FIELD_EQUIPMENTS to equipments,
            FIELD_FORCES to forces,
            FIELD_HANDICAPS to handicaps,
            FIELD_TALENTS to talents,
            FIELD_TYPE to type,
            FIELD_OWNER to owner,
            FIELD_WORLD to world
        )
    }
}

fun List<CharacterDTO>.asDomainModel(): List<Character> {
    return map {
        it.asDomainModel()
    }
}

fun CharacterDTO.asDomainModel(): Character {
    return Character(name, description, charisma, constitution, deception, dexterity, intelligence, investigation, perception, stealth, strength, willpower, movement, parry, toughness, abilities, equipments, forces, handicaps, talents, type, owner, world, id)
}

fun List<Character>.asDatabaseModel(): List<CharacterDTO> {
    return map {
        it.asDatabaseModel()
    }
}

fun Character.asDatabaseModel(): CharacterDTO {
    return CharacterDTO(name, description, charisma, constitution, deception, dexterity, intelligence, investigation, perception, stealth, strength, willpower, movement, parry, toughness, abilities, equipments, forces, handicaps, talents, type, owner, world, id)
}