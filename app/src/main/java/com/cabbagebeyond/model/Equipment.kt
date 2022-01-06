package com.cabbagebeyond.model

data class Equipment(
    var name: String,
    var description: String,
    var cost: String,
    var requirements: List<String>,
    var type: String,
    var world: String,
    val id: String
) {
}

