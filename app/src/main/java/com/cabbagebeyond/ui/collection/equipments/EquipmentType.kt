package com.cabbagebeyond.ui.collection.equipments

import android.content.Context
import com.cabbagebeyond.R
import com.cabbagebeyond.model.Equipment.Type

data class EquipmentType(var type: Type, var title: String) {

    companion object {
        fun create(type: Type, context: Context): EquipmentType {
            val titleId = when (type) {
                Type.WEAPON -> R.string.equipment_type_weapon
                Type.ARMOR -> R.string.equipment_type_armor
                Type.OTHERS -> R.string.equipment_type_others
            }
            val title = context.resources.getString(titleId)
            return EquipmentType(type, title)
        }
    }
}