package com.cabbagebeyond.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cabbagebeyond.data.local.dao.*
import com.cabbagebeyond.data.local.entities.*
import com.cabbagebeyond.data.local.relations.*

@Database(
    entities = [
        AbilityEntity::class,
        CharacterEntity::class,
        CharacterAbilityCrossRef::class,
        CharacterEquipmentCrossRef::class,
        CharacterForceCrossRef::class,
        CharacterHandicapCrossRef::class,
        CharacterTalentCrossRef::class,
        EquipmentEntity::class,
        ForceEntity::class,
        HandicapEntity::class,
        RaceEntity::class,
        RaceFeatureEntity::class,
        RaceFeatureCrossRef::class,
        RoleEntity::class,
        TalentEntity::class,
        UserEntity::class,
        UserRoleCrossRef::class,
        WorldEntity::class,
        StoryEntity::class,
        StoryOwnerCrossRef::class,
        StoryWorldCrossRef::class,
        SessionEntity::class,
        SessionOwnerCrossRef::class,
        SessionPlayersCrossRef::class,
        SessionStoryCrossRef::class,
               ],
    version = 17,
    exportSchema = false
)
@TypeConverters(
    AttributeConverter::class,
    EquipmentTypeConverter::class,
    ListConverter::class,
    TalentRankConverter::class,
    TalentTypeConverter::class
)
abstract class CabbageDatabase : RoomDatabase() {

    abstract fun abilityDao(): AbilityDao
    abstract fun equipmentDao(): EquipmentDao
    abstract fun forceDao(): ForceDao
    abstract fun handicapDao(): HandicapDao
    abstract fun raceDao(): RaceDao
    abstract fun talentDao(): TalentDao
    abstract fun characterDao(): CharacterDao
    abstract fun worldDao(): WorldDao
    abstract fun roleDao(): RoleDao
    abstract fun userDao(): UserDao

    abstract fun storyDao(): StoryDao
    abstract fun sessionDao(): SessionDao

    companion object {
        @Volatile
        private var INSTANCE: CabbageDatabase? = null
        fun getDatabase(context: Context): CabbageDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CabbageDatabase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}