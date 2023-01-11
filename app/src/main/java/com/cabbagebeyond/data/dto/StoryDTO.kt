package com.cabbagebeyond.data.dto

import com.cabbagebeyond.model.Story
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.util.*
import kotlin.collections.HashMap


data class StoryDTO(
    @PropertyName(FIELD_NAME)
    var name: String = "",
    @PropertyName(FIELD_DESCRIPTION)
    var description: String = "",
    @PropertyName(FIELD_STORY)
    var story: String = "",
    @PropertyName(FIELD_OWNER)
    var owner: String = "",
    @PropertyName(FIELD_WORLD)
    var world: String = "",
    @PropertyName(FIELD_RULEBOOK)
    var rulebook: String = "",
    @DocumentId
    val id: String = UUID.randomUUID().toString()
) {

    companion object {
        const val COLLECTION_TITLE = "stories"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_STORY = "story"
        const val FIELD_OWNER = "owner"
        const val FIELD_WORLD = "world_id"
        const val FIELD_RULEBOOK = "ruleset"
    }

    fun toHashMap(): HashMap<String, String> {
        return hashMapOf(
             FIELD_NAME to name,
             FIELD_DESCRIPTION to description,
             FIELD_STORY to story,
             FIELD_OWNER to owner,
             FIELD_WORLD to world
        )
    }
}