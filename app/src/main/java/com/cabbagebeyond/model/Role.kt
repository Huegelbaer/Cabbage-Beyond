package com.cabbagebeyond.model

import android.os.Parcel
import android.os.Parcelable

data class Role(
    var name: String,
    var features: List<String>,
    var id: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeStringList(features)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Role> {
        override fun createFromParcel(parcel: Parcel): Role {
            return Role(parcel)
        }

        override fun newArray(size: Int): Array<Role?> {
            return arrayOfNulls(size)
        }
    }
}