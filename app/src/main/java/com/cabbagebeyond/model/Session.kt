package com.cabbagebeyond.model

data class Session(
    var name: String,
    var description: String,
    var player: String,
    var status: String,
    var invitedPlayers: List<String>,
    var owner: String,
    var story: String,
    val id: String
)