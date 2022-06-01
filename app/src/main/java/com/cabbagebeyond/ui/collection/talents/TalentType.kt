package com.cabbagebeyond.ui.collection.talents

import android.content.Context
import com.cabbagebeyond.R
import com.cabbagebeyond.model.Talent.Type

data class TalentType(var type: Type, var title: String) {

    companion object {
        fun create(type: Type, context: Context): TalentType {
            val titleId = when (type) {
                Type.BACKGROUND -> R.string.talent_type_background
                Type.LEADER -> R.string.talent_type_leader
                Type.EXPERT -> R.string.talent_type_expert
                Type.FIGHT -> R.string.talent_type_fight
                Type.FORCE -> R.string.talent_type_force
                Type.RACE -> R.string.talent_type_race
                Type.SOCIAL -> R.string.talent_type_social
                Type.SUPERNATURAL -> R.string.talent_type_supernatural
                Type.WILDCARD -> R.string.talent_type_wildcard
                Type.LEGENDARY -> R.string.talent_type_legendary
                Type.DRAGON -> R.string.talent_type_dragon
                Type.ATHLETIC -> R.string.talent_type_athletic
                Type.PERCEPTION -> R.string.talent_type_perception
                Type.ILLUSION -> R.string.talent_type_illusion
                Type.PROVOKE -> R.string.talent_type_provoke
                Type.THIEVERY -> R.string.talent_type_thievery
                Type.STEALTH -> R.string.talent_type_stealth
                Type.TERRITORY -> R.string.talent_type_territory
                Type.INTELLECT -> R.string.talent_type_intellect
                Type.STRENGTH -> R.string.talent_type_strength
            }
            val title = context.resources.getString(titleId)
            return TalentType(type, title)
        }
    }
}
