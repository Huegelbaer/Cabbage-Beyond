package com.cabbagebeyond.data.dto

data class ForceDTO(
    var name: String,
    var description: String,
    var cost: String,
    var duration: String,
    var rangRequirement: String,
    var range: String,
    var shaping: String,
    var world: String,
    val id: String
) {

    companion object {
        const val COLLECTION = "sw_forces"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_COST = "cost"
        const val FIELD_DURATION = "duration"
        const val FIELD_RANG_REQUIREMENT = "rang_requirement"
        const val FIELD_RANGE = "range"
        const val FIELD_SHAPING = "shaping"
        const val FIELD_WORLD = "world"
    }
}
