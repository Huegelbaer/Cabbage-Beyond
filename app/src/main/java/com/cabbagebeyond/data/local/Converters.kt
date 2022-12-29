package com.cabbagebeyond.data.local

import androidx.room.TypeConverter
import com.cabbagebeyond.data.local.entities.AbilityEntity

class AttributeConverter {
    @TypeConverter
    fun fromString(value: String?): AbilityEntity.Attribute {
        return when(value) {
            "strength" -> AbilityEntity.Attribute.STRENGTH
            "intellect" -> AbilityEntity.Attribute.INTELLECT
            "constitution" -> AbilityEntity.Attribute.CONSTITUTION
            "dexterity" -> AbilityEntity.Attribute.DEXTERITY
            "willpower" -> AbilityEntity.Attribute.WILLPOWER
            else -> AbilityEntity.Attribute.UNKNOWN
        }
    }

    @TypeConverter
    fun toString(attribute: AbilityEntity.Attribute ): String {
        return when(attribute) {
            AbilityEntity.Attribute.STRENGTH -> "strength"
            AbilityEntity.Attribute.INTELLECT -> "intellect"
            AbilityEntity.Attribute.CONSTITUTION -> "constitution"
            AbilityEntity.Attribute.DEXTERITY -> "dexterity"
            AbilityEntity.Attribute.WILLPOWER -> "willpower"
            AbilityEntity.Attribute.UNKNOWN -> "unknown"
        }
    }
}