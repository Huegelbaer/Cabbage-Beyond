package com.cabbagebeyond.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cabbagebeyond.data.local.dao.WorldDao
import com.cabbagebeyond.data.local.entities.WorldEntity

@Database(entities = [WorldEntity::class], version = 1, exportSchema = false)
abstract class CabbageDatabase : RoomDatabase() {

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