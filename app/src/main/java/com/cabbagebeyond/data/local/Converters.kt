package com.cabbagebeyond.data.local

import androidx.room.TypeConverter
import com.cabbagebeyond.data.local.entities.AbilityEntity
import com.cabbagebeyond.data.local.entities.EquipmentEntity
import com.cabbagebeyond.data.local.entities.TalentEntity
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
        return when (value) {
            "strength" -> AbilityEntity.Attribute.STRENGTH
            "intellect" -> AbilityEntity.Attribute.INTELLECT
            "constitution" -> AbilityEntity.Attribute.CONSTITUTION
            "dexterity" -> AbilityEntity.Attribute.DEXTERITY
            "willpower" -> AbilityEntity.Attribute.WILLPOWER
            else -> AbilityEntity.Attribute.UNKNOWN
        }
    }

    @TypeConverter
    fun toString(attribute: AbilityEntity.Attribute): String {
        return when (attribute) {
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
        return when (value) {
            "weapon" -> EquipmentEntity.Type.WEAPON
            "armor" -> EquipmentEntity.Type.ARMOR
            "others" -> EquipmentEntity.Type.OTHERS
            else -> EquipmentEntity.Type.OTHERS
        }
    }

    @TypeConverter
    fun toString(type: EquipmentEntity.Type): String {
        return when (type) {
            EquipmentEntity.Type.WEAPON -> "weapon"
            EquipmentEntity.Type.ARMOR -> "armor"
            EquipmentEntity.Type.OTHERS -> "others"
        }
    }
}

class TalentRankConverter {
    @TypeConverter
    fun fromString(value: String?): CharacterRank {
        return when (value) {
            "rookie" -> CharacterRank.ROOKIE
            "advanced" -> CharacterRank.ADVANCED
            "hero" -> CharacterRank.HERO
            "legend" -> CharacterRank.LEGEND
            "veteran" -> CharacterRank.VETERAN
            else -> CharacterRank.ROOKIE
        }
    }

    @TypeConverter
    fun toString(rank: CharacterRank): String {
        return when (rank) {
            CharacterRank.ROOKIE -> "rookie"
            CharacterRank.ADVANCED -> "advanced"
            CharacterRank.HERO -> "hero"
            CharacterRank.LEGEND -> "legend"
            CharacterRank.VETERAN -> "veteran"
        }
    }
}

class TalentTypeConverter {
    @TypeConverter
    fun fromString(value: String?): TalentEntity.Type {
        return when (value) {
            "background" -> TalentEntity.Type.BACKGROUND
            "leader" -> TalentEntity.Type.LEADER
            "expert" -> TalentEntity.Type.EXPERT
            "fight" -> TalentEntity.Type.FIGHT
            "force" -> TalentEntity.Type.FORCE
            "race" -> TalentEntity.Type.RACE
            "social" -> TalentEntity.Type.SOCIAL
            "supernatural" -> TalentEntity.Type.SUPERNATURAL
            "wildcard" -> TalentEntity.Type.WILDCARD
            "legendary" -> TalentEntity.Type.LEGENDARY
            "dragon" -> TalentEntity.Type.DRAGON
            "athletic" -> TalentEntity.Type.ATHLETIC
            "perception" -> TalentEntity.Type.PERCEPTION
            "illusion" -> TalentEntity.Type.ILLUSION
            "provoke" -> TalentEntity.Type.PROVOKE
            "thievery" -> TalentEntity.Type.THIEVERY
            "stealth" -> TalentEntity.Type.STEALTH
            "territory" -> TalentEntity.Type.TERRITORY
            "intellect" -> TalentEntity.Type.INTELLECT
            "strength" -> TalentEntity.Type.STRENGTH
            else -> TalentEntity.Type.UNKNOWN
        }
    }

    @TypeConverter
    fun toString(type: TalentEntity.Type): String {
        return when (type) {
            TalentEntity.Type.BACKGROUND -> "background"
            TalentEntity.Type.LEADER -> "leader"
            TalentEntity.Type.EXPERT -> "expert"
            TalentEntity.Type.FIGHT -> "fight"
            TalentEntity.Type.FORCE -> "force"
            TalentEntity.Type.RACE -> "race"
            TalentEntity.Type.SOCIAL -> "social"
            TalentEntity.Type.SUPERNATURAL -> "supernatural"
            TalentEntity.Type.WILDCARD -> "wildcard"
            TalentEntity.Type.LEGENDARY -> "legendary"
            TalentEntity.Type.DRAGON -> "dragon"
            TalentEntity.Type.ATHLETIC -> "athletic"
            TalentEntity.Type.PERCEPTION -> "perception"
            TalentEntity.Type.ILLUSION -> "illusion"
            TalentEntity.Type.PROVOKE -> "provoke"
            TalentEntity.Type.THIEVERY -> "thievery"
            TalentEntity.Type.STEALTH -> "stealth"
            TalentEntity.Type.TERRITORY -> "territory"
            TalentEntity.Type.INTELLECT -> "intellect"
            TalentEntity.Type.STRENGTH -> "strength"
            TalentEntity.Type.UNKNOWN -> ""
        }
    }
}