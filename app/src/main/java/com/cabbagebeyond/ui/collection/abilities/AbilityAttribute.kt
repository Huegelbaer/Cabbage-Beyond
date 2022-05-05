package com.cabbagebeyond.ui.collection.abilities

import android.content.Context
import com.cabbagebeyond.R
import com.cabbagebeyond.model.Attribute

data class AbilityAttribute(var attribute: Attribute, var title: String) {

    companion object {
        fun create(attribute: Attribute, context: Context): AbilityAttribute {
            val titleId = when (attribute) {
                Attribute.STRENGTH -> R.string.attribute_strength
                Attribute.INTELLECT -> R.string.attribute_intellect
                Attribute.CONSTITUTION -> R.string.attribute_constitution
                Attribute.DEXTERITY -> R.string.attribute_dexterity
                Attribute.WILLPOWER -> R.string.attribute_willpower
            }
            val title = context.resources.getString(titleId)
            return AbilityAttribute(attribute, title)
        }
    }
}

