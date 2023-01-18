package com.cabbagebeyond.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cabbagebeyond.data.local.entities.SessionEntity
import com.cabbagebeyond.data.local.relations.SessionWithEverything

@Dao
interface SessionDao {

    @Query("SELECT * FROM session")
    suspend fun getSessions(): List<SessionWithEverything>

    @Query("SELECT * FROM session WHERE id = :id")
    suspend fun getSession(id: String): SessionWithEverything

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSession(session: SessionEntity)

    @Delete
    fun deleteSession(session: SessionEntity)
}