package com.cabbagebeyond.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.cabbagebeyond.data.local.entities.AbilityEntity
import com.cabbagebeyond.data.local.entities.WorldEntity
import com.cabbagebeyond.data.local.entities.asDomainModel
import com.cabbagebeyond.model.Ability
import com.cabbagebeyond.model.Attribute

data class AbilityWithWorld(
    @Embedded
    val ability: AbilityEntity,
    @Relation(
        parentColumn = "world",
        entityColumn = "id"
    )
    val world: WorldEntity?
)

fun List<AbilityWithWorld>.asDomainModel(): List<Ability> {
    return map {
        it.asDomainModel()
    }
}

fun AbilityWithWorld.asDomainModel(): Ability {
    val attribute = when(ability.attribute) {
        AbilityEntity.Attribute.STRENGTH -> Attribute.STRENGTH
        AbilityEntity.Attribute.INTELLECT -> Attribute.INTELLECT
        AbilityEntity.Attribute.CONSTITUTION -> Attribute.CONSTITUTION
        AbilityEntity.Attribute.DEXTERITY -> Attribute.DEXTERITY
        AbilityEntity.Attribute.WILLPOWER -> Attribute.WILLPOWER
        AbilityEntity.Attribute.UNKNOWN -> Attribute.STRENGTH
    }
    return Ability(ability.name, ability.description, attribute, world?.asDomainModel(), ability.id)
}