package com.cabbagebeyond.data.dto


data class UserDTO(
    var name: String,
    var email: String,
    var features: List<String>,
    var roles: List<String>,
    val id: String
) {

    companion object {
        const val COLLECTION = "users"
        const val FIELD_NAME = "name"
        const val FIELD_EMAIL = "email"
        const val FIELD_FEATURES = "features"
        const val FIELD_ROLES = "roles"
    }
}