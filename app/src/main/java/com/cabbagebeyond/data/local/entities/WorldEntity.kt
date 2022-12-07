package com.cabbagebeyond.data.local.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cabbagebeyond.model.World
import java.util.*
import kotlin.collections.HashMap

@Entity(tableName = "world")
data class WorldEntity(
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "description")
    var description: String? = null,
    @ColumnInfo(name = "rulebook")
    var rulebook: String = "",
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString()
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

fun List<WorldEntity>.asDomainModel(): List<World> {
    return map {
        it.asDomainModel()
    }
}

fun WorldEntity.asDomainModel(): World {
    return World(name, description, rulebook, id)
}

fun List<World>.asDatabaseModel(): List<WorldEntity> {
    return map {
        it.asDatabaseModel()
    }
}

fun World.asDatabaseModel(): WorldEntity {
    return WorldEntity(name, description, rulebook, id)
}
