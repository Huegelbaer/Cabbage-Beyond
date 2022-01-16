package com.cabbagebeyond.model

import android.os.Parcel
import android.os.Parcelable


data class Character(
    var name: String,
    var race: String,
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
    var abilities: List<String>,
    var equipments: List<String>,
    var forces: List<String>,
    var handicaps: List<String>,
    var talents: List<String>,
    var type: String,
    var owner: String,
    var world: String,
    val id: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
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
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(race)
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
        parcel.writeStringList(abilities)
        parcel.writeStringList(equipments)
        parcel.writeStringList(forces)
        parcel.writeStringList(handicaps)
        parcel.writeStringList(talents)
        parcel.writeString(type)
        parcel.writeString(owner)
        parcel.writeString(world)
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