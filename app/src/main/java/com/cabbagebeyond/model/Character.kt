package com.cabbagebeyond.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class Character(
    var name: String,
    var race: Race?,
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
    var toughness: String,
    var abilities: List<Ability>,
    var equipments: List<Equipment>,
    var forces: List<Force>,
    var handicaps: List<Handicap>,
    var talents: List<Talent>,
    var type: Type?,
    var owner: String,
    var world: World?,
    val id: String
): Parcelable {

    @Parcelize enum class Type(val value : String) : Parcelable {
        PLAYER("Spieler"), NPC("NPC"), MONSTER("Monster")
    }

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readParcelable(Race::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        listOf(),
        listOf(),
        listOf(),
        listOf(),
        listOf(),
        parcel.readParcelable(Type::class.java.classLoader),
        parcel.readString()!!,
        parcel.readParcelable(World::class.java.classLoader),
        parcel.readString()!!
    ) {
        parcel.readTypedList(abilities, Ability.CREATOR)
        parcel.readTypedList(equipments, Equipment.CREATOR)
        parcel.readTypedList(forces, Force.CREATOR)
        parcel.readTypedList(handicaps, Handicap.CREATOR)
        parcel.readTypedList(talents, Talent.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeParcelable(race, flags)
        parcel.writeString(description)
        parcel.writeInt(charisma)
        parcel.writeString(constitution)
        parcel.writeString(deception)
        parcel.writeString(dexterity)
        parcel.writeString(intelligence)
        parcel.writeString(investigation)
        parcel.writeString(perception)
        parcel.writeString(stealth)
        parcel.writeString(strength)
        parcel.writeString(willpower)
        parcel.writeInt(movement)
        parcel.writeInt(parry)
        parcel.writeString(toughness)
        parcel.writeTypedArray(abilities.toTypedArray(), flags)
        parcel.writeTypedArray(equipments.toTypedArray(), flags)
        parcel.writeTypedArray(forces.toTypedArray(), flags)
        parcel.writeTypedArray(handicaps.toTypedArray(), flags)
        parcel.writeTypedArray(talents.toTypedArray(), flags)
        parcel.writeParcelable(type, flags)
        parcel.writeString(owner)
        parcel.writeParcelable(world, flags)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Character> {
        override fun createFromParcel(parcel: Parcel): Character {
            return Character(parcel)
        }

        override fun newArray(size: Int): Array<Character?> {
            return arrayOfNulls(size)
        }
    }
}