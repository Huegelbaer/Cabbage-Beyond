package com.cabbagebeyond.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectionProperty(var key: String, var displayName: Int, var value: String): Parcelable