package com.cabbagebeyond.model

data class Force(
    var name: String,
    var description: String,
    var cost: String,
    var duration: String,
    var rangRequirement: String,
    var range: String,
    var shaping: String,
    var world: String,
    val id: String
) {
}
