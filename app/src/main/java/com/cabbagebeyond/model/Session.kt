package com.cabbagebeyond.model

data class Session(
    var name: String,
    var description: String,
    var player: String,
    var status: String,
    var invitedPlayers: List<User>,
    var owner: User,
    var story: Story,
    val id: String
)