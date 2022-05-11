package com.cabbagebeyond.ui.collection.handicaps

import android.content.Context
import com.cabbagebeyond.R
import com.cabbagebeyond.model.Handicap

data class HandicapType(var type: Handicap.Type, var title: String) {

    companion object {
        fun create(type: Handicap.Type, context: Context): HandicapType {
            val titleId = when (type) {
                Handicap.Type.SLIGHT -> R.string.talent_type_background
                Handicap.Type.SLIGHT_OR_HEAVY -> R.string.talent_type_leader
                Handicap.Type.HEAVY -> R.string.talent_type_expert
            }
            val title = context.resources.getString(titleId)
            return HandicapType(type, title)
        }
    }
}
