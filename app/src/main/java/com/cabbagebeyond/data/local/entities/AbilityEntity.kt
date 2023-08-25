package com.cabbagebeyond.data.local.entities

import androidx.room.*
import com.cabbagebeyond.model.Ability
import com.cabbagebeyond.model.Attribute
import java.util.*

@Entity(tableName = "ability")
data class AbilityEntity(
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "description")
    var description: String = "",
    @ColumnInfo(name = "attribute")
    var attribute: Attribute,
    @ColumnInfo(name = "world")
    var world: String? = null,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(index = true)
    var id: String = UUID.randomUUID().toString(),
) {
    enum class Attribute {
        STRENGTH,
        INTELLECT,
        CONSTITUTION,
        DEXTERITY,
        WILLPOWER,
        UNKNOWN
    }
}