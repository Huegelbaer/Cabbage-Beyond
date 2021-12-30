package com.cabbagebeyond.data.dto

data class HandicapDTO(
    var name: String,
    var description: String,
    var type: String,
    var world: String,
    val id: String
) {

    companion object {
        const val COLLECTION = "sw_handicaps"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_TYPE = "type"
        const val FIELD_WORLD = "world"
    }
}
