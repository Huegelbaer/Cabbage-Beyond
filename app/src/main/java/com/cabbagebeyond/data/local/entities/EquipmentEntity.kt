package com.cabbagebeyond.data.local.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "equipment")
data class EquipmentEntity(
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "description")
    var description: String = "",
    @ColumnInfo(name = "cost")
    var cost: String = "",
    @ColumnInfo(name = "weight")
    var weight: Double = 0.0,
    @ColumnInfo(name = "requirements")
    var requirements: List<String> = listOf(),
    @ColumnInfo(name = "type")
    var type: Type = Type.OTHERS,
    @ColumnInfo(name = "world")
    var world: String = "",
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString()
) {
    @Parcelize
    enum class Type(val value: String) : Parcelable {
        WEAPON("weapon"),
        ARMOR("armor"),
        OTHERS("others")
    }
}