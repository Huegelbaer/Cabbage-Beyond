package com.cabbagebeyond.data

import com.cabbagebeyond.model.Story

interface StoryDataSource {
    
    //fun observeStories(): LiveData<Result<List<Story>>>

    suspend fun getStories(): Result<List<Story>>

    //suspend fun refreshStories()

    //fun observeStory(id: String): LiveData<Result<Story>>

    suspend fun getStory(id: String): Result<Story>

    //suspend fun refreshStory(id: String)

    suspend fun saveStory(story: Story)

    //suspend fun deleteAllStories()

    suspend fun deleteStory(story: Story)

    /*
        REMOTE
    */

    suspend fun refreshStories()

    suspend fun refreshStory(id: String)
}