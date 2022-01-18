package com.cabbagebeyond.model

import android.os.Parcel
import android.os.Parcelable

data class Handicap(
    var name: String,
    var description: String,
    var type: String,
    var world: String,
    val id: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(type)
        parcel.writeString(world)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Handicap> {
        override fun createFromParcel(parcel: Parcel): Handicap {
            return Handicap(parcel)
        }

        override fun newArray(size: Int): Array<Handicap?> {
            return arrayOfNulls(size)
        }
    }

}
