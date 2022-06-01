package com.cabbagebeyond.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Attribute(val value: String) : Parcelable {
    STRENGTH("Stärke"),
    INTELLECT("Verstand"),
    CONSTITUTION("Konstitution"),
    DEXTERITY("Geschicklichkeit"),
    WILLPOWER("Willenskraft")
}