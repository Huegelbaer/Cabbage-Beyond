package com.cabbagebeyond.data.dto

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
    val id: String
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
        const val FIELD_ABILITIES = "abilities"
        const val FIELD_EQUIPMENTS = "equipments"
        const val FIELD_FORCES = "forces"
        const val FIELD_HANDICAPS = "handicaps"
        const val FIELD_TALENTS = "talents"
        const val FIELD_TYPE = "type"
        const val FIELD_OWNER = "owner"
        const val FIELD_WORLD = "world"
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