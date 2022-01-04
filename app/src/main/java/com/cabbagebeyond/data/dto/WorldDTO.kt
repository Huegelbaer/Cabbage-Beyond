package com.cabbagebeyond.data.dto

data class WorldDTO(
    var name: String,
    var description: String?,
    var rulebook: String,
    val id: String
) {

    companion object {
        const val COLLECTION_TITLE = "worlds"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_RULEBOOK = "rulebook"
    }

    fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            FIELD_NAME to name,
            FIELD_DESCRIPTION to description,
            FIELD_RULEBOOK to rulebook
        )
    }
}