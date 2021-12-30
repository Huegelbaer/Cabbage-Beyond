package com.cabbagebeyond.data.dto

data class AbilityDTO(
    var name: String,
    var description: String,
    var attribute: String,
    var world: String,
    var id: String,
) {

    companion object {
        const val COLLECTION = "sw_abilities"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_ATTRIBUTE = "attribute"
        const val FIELD_WORLD = "id"
    }
}