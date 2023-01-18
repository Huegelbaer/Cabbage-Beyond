package com.cabbagebeyond.model


data class Story(
    var name: String,
    var description: String,
    var story: String,
    var owner: User,
    var world: World?,
    val id: String
)