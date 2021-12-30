package com.cabbagebeyond.data.dto

data class EquipmentDTO(
    var name: String,
    var description: String,
    var cost: String,
    var requirements: List<String>,
    var type: String,
    var world: String,
    val id: String
) {

    companion object {
        const val COLLECTION = "sw_equipments"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_COST = "cost"
        const val FIELD_REQUIREMENTS = "requirements"
        const val FIELD_TYPE = "type"
        const val FIELD_WORLD = "world"
    }
}

