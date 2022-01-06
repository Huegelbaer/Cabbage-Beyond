package com.cabbagebeyond.model

import android.os.Parcel
import android.os.Parcelable

data class World(
    var name: String,
    var description: String?,
    var rulebook: String,
    val id: String
) : Parcelable {

    private constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(rulebook)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<World> {
        override fun createFromParcel(parcel: Parcel): World {
            return World(parcel)
        }

        override fun newArray(size: Int): Array<World?> {
            return arrayOfNulls(size)
        }
    }

}