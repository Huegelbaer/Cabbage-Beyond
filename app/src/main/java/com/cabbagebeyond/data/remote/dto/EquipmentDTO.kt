package com.cabbagebeyond.data.remote.dto

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.util.*
import kotlin.collections.HashMap

data class EquipmentDTO(
    @PropertyName(FIELD_NAME)
    var name: String = "",
    @PropertyName(FIELD_DESCRIPTION)
    var description: String = "",
    @PropertyName(FIELD_COST)
    var cost: String = "",
    @PropertyName(FIELD_WEIGHT)
    var weight: Double = 0.0,
    @PropertyName(FIELD_REQUIREMENTS)
    var requirements: String = "",
    @PropertyName(FIELD_TYPE)
    var type: String = "",
    @PropertyName(FIELD_WORLD)
    var world: String = "",
    @DocumentId
    val id: String = UUID.randomUUID().toString()
) {

    companion object {
        const val COLLECTION_TITLE = "sw_equipments"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_COST = "cost"
        const val FIELD_WEIGHT = "weight"
        const val FIELD_REQUIREMENTS = "requirements"
        const val FIELD_TYPE = "type"
        const val FIELD_WORLD = "world_id"
    }

    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            FIELD_NAME to name,
            FIELD_DESCRIPTION to description,
            FIELD_COST to cost,
            FIELD_WEIGHT to weight,
            FIELD_REQUIREMENTS to requirements,
            FIELD_TYPE to type,
            FIELD_WORLD to world
        )
    }
}