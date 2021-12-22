package com.cabbagebeyond.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cabbagebeyond.data.dto.*

@Database(
    entities = [
        AbilityDTO::class,
        CharacterDTO::class,
        EquipmentDTO::class,
        ForceDTO::class,
        HandicapDTO::class,
        RaceDTO::class,
        RoleDTO::class,
        SessionDTO::class,
        StoryDTO::class,
        TalentDTO::class,
        UserDTO::class,
        WorldDTO::class
    ],
    version = 1
)
public abstract class CabbageDatabase: RoomDatabase() {

//    abstract fun abilityDao(): AbilityDAO
}