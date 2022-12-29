package com.cabbagebeyond.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cabbagebeyond.data.local.dao.*
import com.cabbagebeyond.data.local.entities.*

@Database(
    entities = [AbilityEntity::class, EquipmentEntity::class, ForceEntity::class, HandicapEntity::class, WorldEntity::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(AttributeConverter::class, EquipmentTypeConverter::class, ListConverter::class)
abstract class CabbageDatabase : RoomDatabase() {

    abstract fun abilityDao(): AbilityDao
    abstract fun equipmentDao(): EquipmentDao
    abstract fun forceDao(): ForceDao
    abstract fun handicapDao(): HandicapDao
    abstract fun worldDao(): WorldDao

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