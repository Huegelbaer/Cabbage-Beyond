package com.cabbagebeyond.data.dto

import com.cabbagebeyond.model.Race
import com.cabbagebeyond.model.User
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
    var raceFeatures: List<String> = listOf(),
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

    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            FIELD_NAME to name,
            FIELD_DESCRIPTION to description,
            FIELD_RACE_FEATURES to raceFeatures,
            FIELD_WORLD to world,
        )
    }
}

fun List<RaceDTO>.asDomainModel(): List<Race> {
    return map {
        it.asDomainModel()
    }
}

fun RaceDTO.asDomainModel(): Race {
    return Race(name, description, raceFeatures, world, id)
}

fun List<Race>.asDatabaseModel(): List<RaceDTO> {
    return map {
        it.asDatabaseModel()
    }
}

fun Race.asDatabaseModel(): RaceDTO {
    return RaceDTO(name, description, raceFeatures, world, id)
}