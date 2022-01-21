package com.cabbagebeyond.data.dto

import com.cabbagebeyond.model.Ability
import com.cabbagebeyond.model.User
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.util.*
import kotlin.collections.HashMap

data class AbilityDTO(
    @PropertyName(FIELD_NAME)
    var name: String = "",
    @PropertyName(FIELD_DESCRIPTION)
    var description: String = "",
    @PropertyName(FIELD_ATTRIBUTE)
    var attribute: String = "",
    @PropertyName(FIELD_WORLD)
    var world: String = "",
    @DocumentId
    var id: String = UUID.randomUUID().toString(),
) {

    companion object {
        const val COLLECTION_TITLE = "sw_abilities"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_ATTRIBUTE = "attribute"
        const val FIELD_WORLD = "world_id"
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
