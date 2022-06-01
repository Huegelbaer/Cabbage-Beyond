package com.cabbagebeyond.model

import android.os.Parcel
import android.os.Parcelable

data class Ability(
    var name: String,
    var description: String,
    var attribute: Attribute,
    var world: World?,
    var id: String,
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(Attribute::class.java.classLoader)!!,
        parcel.readParcelable(World::class.java.classLoader),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeParcelable(attribute, flags)
        parcel.writeParcelable(world, flags)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ability> {
        override fun createFromParcel(parcel: Parcel): Ability {
            return Ability(parcel)
        }

        override fun newArray(size: Int): Array<Ability?> {
            return arrayOfNulls(size)
        }
    }
}