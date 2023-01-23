package com.cabbagebeyond.data.remote.dto

import com.cabbagebeyond.model.Role
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.util.*
import kotlin.collections.HashMap

data class RoleDTO(
    @PropertyName(FIELD_NAME)
    var name: String = "",
    @PropertyName(FIELD_FEATURES)
    var features: List<String> = listOf(),
    @DocumentId
    var id: String = UUID.randomUUID().toString()
) {

    companion object {
        const val COLLECTION_TITLE = "roles"
        const val FIELD_NAME = "role"
        const val FIELD_FEATURES = "features"
    }

    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            FIELD_NAME to name,
            FIELD_FEATURES to features
        )
    }
}
