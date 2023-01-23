package com.cabbagebeyond.data

import com.cabbagebeyond.model.Story

interface StoryDataSource {

    suspend fun getStories(): Result<List<Story>>

    suspend fun getStory(id: String): Result<Story>

    suspend fun saveStory(story: Story)

    suspend fun deleteStory(story: Story)

    /*
        REMOTE
    */

    suspend fun refreshStories()

    suspend fun refreshStory(id: String)
}