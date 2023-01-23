package com.cabbagebeyond.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Rank : Parcelable {
    ROOKIE,
    ADVANCED,
    VETERAN,
    HERO,
    LEGEND
}
