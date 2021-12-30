package com.cabbagebeyond.data.dto

data class RoleDTO(
    var name: String,
    var features: List<String>,
    var id: String
) {

    companion object {
        const val COLLECTION = "roles"
        const val FIELD_NAME = "name"
        const val FIELD_FEATURES = "features"
    }
}