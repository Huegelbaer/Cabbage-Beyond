package com.cabbagebeyond.model

data class Race(
    var name: String,
    var description: String,
    var raceFeatures: List<String>,
    var world: String,
    val id: String
) {
}
