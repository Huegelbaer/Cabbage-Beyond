package com.cabbagebeyond.model

import android.os.Parcel
import android.os.Parcelable

data class Race(
    var name: String,
    var description: String,
    var raceFeatures: List<Feature>,
    var world: World?,
    val id: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        arrayListOf<Feature>().apply {
            parcel.readList(this, Feature::class.java.classLoader)
        },
        parcel.readParcelable(World::class.java.classLoader)!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeList(raceFeatures)
        parcel.writeParcelable(world, flags)
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

    data class Feature(
        var name: String,
        var description: String,
        val id: String
    ) : Parcelable {

        constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(name)
            parcel.writeString(description)
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
}
