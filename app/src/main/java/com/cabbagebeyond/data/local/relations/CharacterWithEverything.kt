package com.cabbagebeyond.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cabbagebeyond.data.local.entities.*

data class CharacterWithEverything(
    @Embedded
    val character: CharacterEntity,
    @Relation(
        entity = AbilityEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = CharacterAbilityCrossRef::class,
            parentColumn = "character_id",
            entityColumn = "ability_id"
        )
    )
    val abilities: List<AbilityEntity>,
    @Relation(
        entity = EquipmentEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = CharacterEquipmentCrossRef::class,
            parentColumn = "character_id",
            entityColumn = "equipment_id"
        )
    )
    val equipments : List<EquipmentEntity>,
    @Relation(
        entity = ForceEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = CharacterForceCrossRef::class,
            parentColumn = "character_id",
            entityColumn = "force_id"
        )
    )
    val forces : List<ForceEntity>,
    @Relation(
        entity = HandicapEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = CharacterHandicapCrossRef::class,
            parentColumn = "character_id",
            entityColumn = "handicap_id"
        )
    )
    var handicaps : List<HandicapEntity>,
    @Relation(
        entity = TalentEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = CharacterTalentCrossRef::class,
            parentColumn = "character_id",
            entityColumn = "talent_id"
        )
    )
    var talents : List<TalentEntity>,
    @Relation(
        parentColumn = "race",
        entityColumn = "id"
    )
    val race: RaceEntity?,
    @Relation(
        parentColumn = "world",
        entityColumn = "id"
    )
    val world: WorldEntity?
)