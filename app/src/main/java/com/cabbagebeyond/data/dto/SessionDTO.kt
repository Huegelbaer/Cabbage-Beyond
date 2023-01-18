package com.cabbagebeyond.data.dto

import com.cabbagebeyond.model.Session
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.util.*
import kotlin.collections.HashMap

data class SessionDTO(
    @PropertyName(FIELD_NAME)
    var name: String = "",
    @PropertyName(FIELD_DESCRIPTION)
    var description: String = "",
    @PropertyName(FIELD_PLAYER)
    var player: String = "",
    @PropertyName(FIELD_STATUS)
    var status: String = "",
    @PropertyName(FIELD_INVITED_PLAYERS)
    var invitedPlayers: List<String> = listOf(),
    @PropertyName(FIELD_OWNER)
    var owner: String = "",
    @PropertyName(FIELD_STORY)
    var story: String = "",
    @PropertyName(FIELD_RULEBOOK)
    var rulebook: String = "",
    @DocumentId
    val id: String= UUID.randomUUID().toString()
) {

    companion object {
        const val COLLECTION_TITLE = "sessions"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_PLAYER = "player"
        const val FIELD_STATUS = "status"
        const val FIELD_INVITED_PLAYERS = "invitedUsers"
        const val FIELD_OWNER = "owner"
        const val FIELD_STORY = "story_id"
        const val FIELD_RULEBOOK = "ruleset"
    }

    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            FIELD_NAME to name,
            FIELD_DESCRIPTION to description,
            FIELD_PLAYER to player,
            FIELD_STATUS to status,
            FIELD_INVITED_PLAYERS to invitedPlayers,
            FIELD_OWNER to owner,
            FIELD_STORY to story,
        )
    }
}