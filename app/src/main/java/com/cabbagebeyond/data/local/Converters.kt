package com.cabbagebeyond.data.local

import androidx.room.TypeConverter
import com.cabbagebeyond.data.local.entities.AbilityEntity
import com.cabbagebeyond.data.local.entities.EquipmentEntity
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.reflect.Type

class ListConverter {
    @TypeConverter
    fun fromString(value: String?): List<String?>? {
        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(list: List<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}

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

class EquipmentTypeConverter {
    @TypeConverter
    fun fromString(value: String?): EquipmentEntity.Type {
        return when(value) {
            "weapon" -> EquipmentEntity.Type.WEAPON
            "armor" -> EquipmentEntity.Type.ARMOR
            "others" -> EquipmentEntity.Type.OTHERS
            else -> EquipmentEntity.Type.OTHERS
        }
    }

    @TypeConverter
    fun toString(type: EquipmentEntity.Type): String {
        return when(type) {
            EquipmentEntity.Type.WEAPON -> "weapon"
            EquipmentEntity.Type.ARMOR -> "armor"
            EquipmentEntity.Type.OTHERS -> "others"
        }
    }
}