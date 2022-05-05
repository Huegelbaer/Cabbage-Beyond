package com.cabbagebeyond.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class Talent(
    var name: String,
    var description: String,
    var rangRequirement: Rank?,
    var requirements: List<String>,
    var type: Type?,
    var world: World?,
    val id: String
): Parcelable {

    @Parcelize
    enum class Type(val value: String) : Parcelable {
        BACKGROUND("Hintergrundtalent"),
        LEADER("Anführertalent"),
        EXPERT("Expertentalent"),
        FIGHT("Kampftalent"),
        FORCE("Machttalent"),
        RACE("Rassentalent"),
        SOCIAL("Soziales Talent"),
        SUPERNATURAL("Übernatürliches Talent"),
        WILDCARD("Wildcard Talent"),
        LEGENDARY("Legendäres Talent"),
        DRAGON("Drachentalent"),
        ATHLETIC("Athletiktalent"),
        PERCEPTION("Wahrnehmungstalent"),
        ILLUSION("Täuschungstalent"),
        PROVOKE("Provozierentalent"),
        THIEVERY("Diebeskunsttalent"),
        STEALTH("Heimlichkeitstalent"),
        TERRITORY("Reviertalent"),
        INTELLECT("Verstandstalent"),
        STRENGTH("Krafttalent")
    }


    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(Rank::class.java.classLoader)!!,
        parcel.createStringArrayList()!!,
        parcel.readParcelable(Type::class.java.classLoader)!!,
        parcel.readParcelable(World::class.java.classLoader)!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeParcelable(rangRequirement, flags)
        parcel.writeStringList(requirements)
        parcel.writeParcelable(type, flags)
        parcel.writeParcelable(world, flags)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Talent> {
        override fun createFromParcel(parcel: Parcel): Talent {
            return Talent(parcel)
        }

        override fun newArray(size: Int): Array<Talent?> {
            return arrayOfNulls(size)
        }
    }
}
