package com.cabbagebeyond.data.dto

import com.cabbagebeyond.model.Force
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.util.*
import kotlin.collections.HashMap

data class ForceDTO(
    @PropertyName(FIELD_NAME)
    var name: String = "",
    @PropertyName(FIELD_DESCRIPTION)
    var description: String = "",
    @PropertyName(FIELD_COST)
    var cost: String = "",
    @PropertyName(FIELD_DURATION)
    var duration: String = "",
    @PropertyName(FIELD_RANG_REQUIREMENT)
    var rangRequirement: String = "",
    @PropertyName(FIELD_RANGE)
    var range: String = "",
    @PropertyName(FIELD_SHAPING)
    var shaping: String = "",
    @PropertyName(FIELD_WORLD)
    var world: String = "",
    @DocumentId
    val id: String = UUID.randomUUID().toString()
) {

    companion object {
        const val COLLECTION_TITLE = "sw_forces"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_COST = "cost"
        const val FIELD_DURATION = "duration"
        const val FIELD_RANG_REQUIREMENT = "rangRequirement"
        const val FIELD_RANGE = "range"
        const val FIELD_SHAPING = "shaping"
        const val FIELD_WORLD = "world_id"
    }

    fun toHashMap(): HashMap<String, String> {
        return hashMapOf(
            FIELD_NAME to name,
            FIELD_DESCRIPTION to description,
            FIELD_COST to cost,
            FIELD_DURATION to duration,
            FIELD_RANG_REQUIREMENT to rangRequirement,
            FIELD_RANGE to range,
            FIELD_SHAPING to shaping,
            FIELD_WORLD to world
        )
    }
}