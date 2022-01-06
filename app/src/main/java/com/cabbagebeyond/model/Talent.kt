package com.cabbagebeyond.model


data class Talent(
    var name: String,
    var description: String,
    var rangRequirement: String,
    var requirements: List<String>,
    var type: String,
    var world: String,
    val id: String
) {
}
