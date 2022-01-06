package com.cabbagebeyond.data.dto

import com.cabbagebeyond.model.World
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class WorldDTO(
    @PropertyName(FIELD_NAME)
    var name: String = "",
    @PropertyName(FIELD_DESCRIPTION)
    var description: String? = null,
    @PropertyName(FIELD_RULEBOOK)
    var rulebook: String = "",
    @DocumentId
    val id: String = ""
) {

    companion object {
        const val COLLECTION_TITLE = "worlds"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_RULEBOOK = "ruleset"
    }

    fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            FIELD_NAME to name,
            FIELD_DESCRIPTION to description,
            FIELD_RULEBOOK to rulebook
        )
    }

}

fun List<WorldDTO>.asDomainModel(): List<World> {
    return map {
        it.asDomainModel()
    }
}

fun WorldDTO.asDomainModel(): World {
    return World(name, description, rulebook, id)
}

fun List<World>.asDatabaseModel(): List<WorldDTO> {
    return map {
        it.asDatabaseModel()
    }
}

fun World.asDatabaseModel(): WorldDTO {
    return WorldDTO(name, description, rulebook, id)
}
