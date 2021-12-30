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
        const val COLLECTION = "stories"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_STORY = "story"
        const val FIELD_OWNER = "owner"
        const val FIELD_WORLD = "world"
    }
}