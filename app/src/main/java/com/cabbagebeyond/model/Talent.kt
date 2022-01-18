package com.cabbagebeyond.model

import android.os.Parcel
import android.os.Parcelable


data class Talent(
    var name: String,
    var description: String,
    var rangRequirement: String,
    var requirements: List<String>,
    var type: String,
    var world: String,
    val id: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(rangRequirement)
        parcel.writeStringList(requirements)
        parcel.writeString(type)
        parcel.writeString(world)
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
