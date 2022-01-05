package com.cabbagebeyond.data.dto

data class AbilityDTO(
    var name: String,
    var description: String,
    var attribute: String,
    var world: String,
    var id: String,
) {

    companion object {
        const val COLLECTION_TITLE = "sw_abilities"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_ATTRIBUTE = "attribute"
        const val FIELD_WORLD = "id"
    }

    fun toHashMap(): HashMap<String, String> {
        return hashMapOf(
            FIELD_NAME to name,
            FIELD_DESCRIPTION to description,
            FIELD_ATTRIBUTE to attribute,
            FIELD_WORLD to world
        )
    }
}