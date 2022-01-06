package com.cabbagebeyond.model


data class Character(
    var name: String,
    var description: String,
    var charisma: Int,
    var constitution: String,
    var deception: String,
    var dexterity: String,
    var intelligence: String,
    var investigation: String,
    var perception: String,
    var stealth: String,
    var strength: String,
    var willpower: String,
    var movement: Int,
    var parry: Int,
    var toughness: Int,
    var abilities: List<String>,
    var equipments: List<String>,
    var forces: List<String>,
    var handicaps: List<String>,
    var talents: List<String>,
    var type: String,
    var owner: String,
    var world: String,
    val id: String
) {
}