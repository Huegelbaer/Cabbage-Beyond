package com.cabbagebeyond.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Handicap(
    var name: String,
    var description: String,
    var type: Type,
    var world: World?,
    val id: String
): Parcelable {

    @Parcelize
    enum class Type(val value: String) : Parcelable {
        SLIGHT("Leicht"),
        SLIGHT_OR_HEAVY("Leicht/Schwer"),
        HEAVY("Schwer"),
    }

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(Type::class.java.classLoader)!!,
        parcel.readParcelable(World::class.java.classLoader)!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeParcelable(type, flags)
        parcel.writeParcelable(world, flags)
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
