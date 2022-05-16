package com.cabbagebeyond.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Equipment(
    var name: String,
    var description: String,
    var cost: String,
    var weight: Double,
    var requirements: List<String>,
    var type: Type?,
    var world: World?,
    val id: String
): Parcelable {

    @Parcelize
    enum class Type(val value: String) : Parcelable {
        WEAPON("Waffe"),
        ARMOR("Rüstung"),
        OTHERS("Sonstiges")
    }

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.createStringArrayList()!!,
        parcel.readParcelable<Type>(Type::class.java.classLoader)!!,
        parcel.readParcelable(World::class.java.classLoader),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(cost)
        parcel.writeDouble(weight)
        parcel.writeStringList(requirements)
        parcel.writeParcelable(type, flags)
        parcel.writeParcelable(world, flags)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Equipment> {
        override fun createFromParcel(parcel: Parcel): Equipment {
            return Equipment(parcel)
        }

        override fun newArray(size: Int): Array<Equipment?> {
            return arrayOfNulls(size)
        }
    }

}