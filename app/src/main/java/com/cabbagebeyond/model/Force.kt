package com.cabbagebeyond.model

import android.os.Parcel
import android.os.Parcelable

data class Force(
    var name: String,
    var description: String,
    var cost: String,
    var duration: String,
    var rangRequirement: Rank?,
    var range: String,
    var shaping: String,
    var world: World?,
    val id: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable<Rank>(Rank::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable<World>(World::class.java.classLoader)!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(cost)
        parcel.writeString(duration)
        parcel.writeParcelable(rangRequirement, flags)
        parcel.writeString(range)
        parcel.writeString(shaping)
        parcel.writeParcelable(world, flags)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Force> {
        override fun createFromParcel(parcel: Parcel): Force {
            return Force(parcel)
        }

        override fun newArray(size: Int): Array<Force?> {
            return arrayOfNulls(size)
        }
    }

}