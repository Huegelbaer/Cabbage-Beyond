package com.cabbagebeyond.data.dto

import com.cabbagebeyond.model.User
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.util.*
import kotlin.collections.HashMap


data class UserDTO(
    @PropertyName(FIELD_NAME)
    var username: String = "",
    @PropertyName(FIELD_EMAIL)
    var email: String = "",
    @PropertyName(FIELD_FEATURES)
    var features: List<String> = listOf(),
    @PropertyName(FIELD_ROLES)
    var roles_ids: List<String> = listOf(),
    @DocumentId
    val id: String = UUID.randomUUID().toString()
) {

    companion object {
        const val COLLECTION_TITLE = "users"
        const val FIELD_NAME = "username"
        const val FIELD_EMAIL = "email"
        const val FIELD_FEATURES = "features"
        const val FIELD_ROLES = "roles_ids"
    }

    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            FIELD_NAME to username,
            FIELD_EMAIL to email,
            FIELD_FEATURES to features,
            FIELD_ROLES to roles_ids
        )
    }
}
