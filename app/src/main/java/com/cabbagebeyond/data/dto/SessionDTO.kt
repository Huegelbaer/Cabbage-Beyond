package com.cabbagebeyond.data.dto

data class SessionDTO(
    var name: String,
    var description: String,
    var player: String,
    var status: String,
    var invitedPlayers: List<String>,
    var owner: String,
    var story: String,
    val id: String
) {

    companion object {
        const val COLLECTION = "sessions"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_PLAYER = "player"
        const val FIELD_STATUS = "status"
        const val FIELD_INVITED_PLAYERS = "invited_players"
        const val FIELD_OWNER = "owner"
        const val FIELD_STORY = "story"
    }
}