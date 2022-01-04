package com.cabbagebeyond.data.dto


data class StoryDTO(
    var name: String,
    var description: String,
    var story: String,
    var owner: String,
    var world: String,
    val id: String
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