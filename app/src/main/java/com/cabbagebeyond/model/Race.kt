package com.cabbagebeyond.model

import android.os.Parcel
import android.os.Parcelable

data class Race(
    var name: String,
    var description: String,
    var raceFeatures: List<String>,
    var world: String,
    val id: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeStringList(raceFeatures)
        parcel.writeString(world)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Race> {
        override fun createFromParcel(parcel: Parcel): Race {
            return Race(parcel)
        }

        override fun newArray(size: Int): Array<Race?> {
            return arrayOfNulls(size)
        }
    }
}
