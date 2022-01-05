package com.cabbagebeyond.data.dto

data class RaceDTO(
    var name: String,
    var description: String,
    var raceFeatures: List<String>,
    var world: String,
    val id: String
) {

    companion object {
        const val COLLECTION_TITLE = "sw_races"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_RACE_FEATURES = "race_features"
        const val FIELD_WORLD = "world"
    }

    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            FIELD_NAME to name,
            FIELD_DESCRIPTION to description,
            FIELD_RACE_FEATURES to raceFeatures,
            FIELD_WORLD to world,
        )
    }
}
