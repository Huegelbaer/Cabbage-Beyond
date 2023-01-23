package com.cabbagebeyond.data.remote.dto

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.util.*
import kotlin.collections.HashMap

data class RaceDTO(
    @PropertyName(FIELD_NAME)
    var name: String = "",
    @PropertyName(FIELD_DESCRIPTION)
    var description: String = "",
    @PropertyName(FIELD_RACE_FEATURES)
    var raceFeatures: List<Feature> = listOf(),
    @PropertyName(FIELD_WORLD)
    var world: String = "",
    @DocumentId
    val id: String = UUID.randomUUID().toString()
) {

    companion object {
        const val COLLECTION_TITLE = "sw_races"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_RACE_FEATURES = "racefeats"
        const val FIELD_WORLD = "world_id"
    }

    data class Feature(
        @PropertyName(FIELD_NAME)
        var name: String,
        @PropertyName(FIELD_DESCRIPTION)
        var description: String,
        @DocumentId
        val id: String = UUID.randomUUID().toString())

    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            FIELD_NAME to name,
            FIELD_DESCRIPTION to description,
            FIELD_RACE_FEATURES to raceFeatures,
            FIELD_WORLD to world,
        )
    }
}