package com.cabbagebeyond.data

import androidx.lifecycle.LiveData
import com.cabbagebeyond.data.dto.StoryDTO

interface StoryDataSource {
    
    fun observeStories(): LiveData<Result<List<StoryDTO>>>

    suspend fun getStories(): Result<List<StoryDTO>>

    suspend fun refreshStories()

    fun observeStory(id: String): LiveData<Result<StoryDTO>>

    suspend fun getStory(id: String): Result<StoryDTO>

    suspend fun refreshStory(id: String)

    suspend fun saveStory(world: StoryDTO)

    suspend fun deleteAllStories()

    suspend fun deleteStory(id: String)
}