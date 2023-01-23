package com.cabbagebeyond.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cabbagebeyond.data.local.entities.StoryEntity
import com.cabbagebeyond.data.local.relations.StoryOwnerCrossRef
import com.cabbagebeyond.data.local.relations.StoryWithEverything
import com.cabbagebeyond.data.local.relations.StoryWorldCrossRef

@Dao
interface StoryDao {

    @Query("SELECT * FROM story")
    suspend fun getStories(): List<StoryWithEverything>

    @Query("SELECT * FROM story WHERE id = :id")
    suspend fun getStory(id: String): StoryWithEverything

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStory(story: StoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOwner(story: StoryOwnerCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWorld(story: StoryWorldCrossRef)

    @Delete
    suspend fun deleteStory(story: StoryEntity)
}