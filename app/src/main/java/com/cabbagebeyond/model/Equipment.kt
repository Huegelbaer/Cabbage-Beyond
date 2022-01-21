package com.cabbagebeyond.model

import android.os.Parcel
import android.os.Parcelable

data class Equipment(
    var name: String,
    var description: String,
    var cost: String,
    var requirements: List<String>,
    var type: String,
    var world: World?,
    val id: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readString()!!,
        parcel.readParcelable(World::class.java.classLoader),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(cost)
        parcel.writeStringList(requirements)
        parcel.writeString(type)
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