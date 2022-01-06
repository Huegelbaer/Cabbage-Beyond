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
    @DocumentId
    val id: String = UUID.randomUUID().toString()
) {

    companion object {
        const val COLLECTION_TITLE = "stories"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_STORY = "story"
        const val FIELD_OWNER = "owner"
        const val FIELD_WORLD = "world"
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
fun List<StoryDTO>.asDomainModel(): List<Story> {
    return map {
        it.asDomainModel()
    }
}

fun StoryDTO.asDomainModel(): Story {
    return Story(name, description, story, owner, world, id)
}

fun List<Story>.asDatabaseModel(): List<StoryDTO> {
    return map {
        it.asDatabaseModel()
    }
}

fun Story.asDatabaseModel(): StoryDTO {
    return StoryDTO(name, description, story, owner, world, id)
}