package com.cabbagebeyond.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Rank(val value: String) : Parcelable {
    ROOKIE("Anfänger"),
    ADVANCED("Fortgeschritten"),
    VETERAN("Veteran"),
    HERO("Held"),
    LEGEND("Legende")
}
