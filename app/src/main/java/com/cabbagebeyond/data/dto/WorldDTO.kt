package com.cabbagebeyond.data.dto

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class WorldDTO(
    @PropertyName(FIELD_NAME)
    var name: String = "",
    @PropertyName(FIELD_DESCRIPTION)
    var description: String? = null,
    @PropertyName(FIELD_RULEBOOK)
    var rulebook: String = "",
    @DocumentId
    val id: String = ""
) {

    companion object {
        const val COLLECTION_TITLE = "worlds"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_RULEBOOK = "ruleset"
    }

    fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            FIELD_NAME to name,
            FIELD_DESCRIPTION to description,
            FIELD_RULEBOOK to rulebook
        )
    }
}