package com.cabbagebeyond.data.dto


data class TalentDTO(
    var name: String,
    var description: String,
    var rangRequirement: String,
    var requirements: List<String>,
    var type: String,
    var world: String,
    val id: String
) {

    companion object {
        const val COLLECTION_TITLE = "sw_talents"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_RANG_REQUIREMENT = "rang_requirement"
        const val FIELD_REQUIREMENTS = "requirements"
        const val FIELD_TYPE = "type"
        const val FIELD_WORLD = "world"
    }

    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            FIELD_NAME to name,
            FIELD_DESCRIPTION to description,
            FIELD_RANG_REQUIREMENT to rangRequirement,
            FIELD_REQUIREMENTS to requirements,
            FIELD_TYPE to type,
            FIELD_WORLD to world
        )
    }
}
