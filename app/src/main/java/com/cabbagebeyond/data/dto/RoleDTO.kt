package com.cabbagebeyond.data.dto

data class RoleDTO(
    var name: String,
    var features: List<String>,
    var id: String
) {

    companion object {
        const val COLLECTION_TITLE = "roles"
        const val FIELD_NAME = "name"
        const val FIELD_FEATURES = "features"
    }

    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            FIELD_NAME to name,
            FIELD_FEATURES to features
        )
    }
}