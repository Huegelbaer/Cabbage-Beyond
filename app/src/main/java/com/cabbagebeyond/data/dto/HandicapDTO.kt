package com.cabbagebeyond.data.dto

import com.cabbagebeyond.model.Handicap
import java.util.*
import kotlin.collections.HashMap

data class HandicapDTO(
    var name: String,
    var description: String,
    var type: String,
    var world: String,
    val id: String = UUID.randomUUID().toString()
) {

    companion object {
        const val COLLECTION_TITLE = "sw_handicaps"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_TYPE = "type"
        const val FIELD_WORLD = "world"
    }

    fun toHashMap(): HashMap<String, String> {
        return hashMapOf(
            FIELD_NAME to name,
            FIELD_DESCRIPTION to description,
            FIELD_TYPE to type,
            FIELD_WORLD to world
        )
    }
}

fun List<HandicapDTO>.asDomainModel(): List<Handicap> {
    return map {
        it.asDomainModel()
    }
}

fun HandicapDTO.asDomainModel(): Handicap {
    return Handicap(name, description, type, world, id)
}

fun List<Handicap>.asDatabaseModel(): List<HandicapDTO> {
    return map {
        it.asDatabaseModel()
    }
}

fun Handicap.asDatabaseModel(): HandicapDTO {
    return HandicapDTO(name, description, type, world, id)
}