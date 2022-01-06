package com.cabbagebeyond.data.dto

import com.cabbagebeyond.model.Talent
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.util.*
import kotlin.collections.HashMap


data class TalentDTO(
    @PropertyName(FIELD_NAME)
    var name: String = "",
    @PropertyName(FIELD_DESCRIPTION)
    var description: String = "",
    @PropertyName(FIELD_RANG_REQUIREMENT)
    var rangRequirement: String = "",
    @PropertyName(FIELD_REQUIREMENTS)
    var requirements: List<String> = listOf(),
    @PropertyName(FIELD_TYPE)
    var type: String = "",
    @PropertyName(FIELD_WORLD)
    var world: String = "",
    @DocumentId
    val id: String = UUID.randomUUID().toString()
) {

    companion object {
        const val COLLECTION_TITLE = "sw_talents"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_RANG_REQUIREMENT = "rang_requirement"
        const val FIELD_REQUIREMENTS = "requirements"
        const val FIELD_TYPE = "type"
        const val FIELD_WORLD = "world"
    }

    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            FIELD_NAME to name,
            FIELD_DESCRIPTION to description,
            FIELD_RANG_REQUIREMENT to rangRequirement,
            FIELD_REQUIREMENTS to requirements,
            FIELD_TYPE to type,
            FIELD_WORLD to world
        )
    }
}

fun List<TalentDTO>.asDomainModel(): List<Talent> {
    return map {
        it.asDomainModel()
    }
}

fun TalentDTO.asDomainModel(): Talent {
    return Talent(name, description, rangRequirement, requirements, type, world, id)
}

fun List<Talent>.asDatabaseModel(): List<TalentDTO> {
    return map {
        it.asDatabaseModel()
    }
}

fun Talent.asDatabaseModel(): TalentDTO {
    return TalentDTO(name, description, rangRequirement, requirements, type, world, id)
}