package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.StoryDataSource
import com.cabbagebeyond.data.local.dao.StoryDao
import com.cabbagebeyond.data.dto.asDatabaseModel
import com.cabbagebeyond.data.dto.asDomainModel
import com.cabbagebeyond.model.Story
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StoryRepository(
    private val storyDao: StoryDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : StoryDataSource {

    override suspend fun getStories(): Result<List<Story>> = withContext(ioDispatcher) {
        return@withContext storyDao.getStories().mapCatching { it.asDomainModel() }
    }

    override suspend fun getStory(id: String): Result<Story> = withContext(ioDispatcher) {
        return@withContext storyDao.getStory(id).mapCatching { it.asDomainModel() }
    }

    override suspend fun saveStory(story: Story) = withContext(ioDispatcher) {
        storyDao.saveStory(story.asDatabaseModel())
    }

    override suspend fun deleteStory(id: String) = withContext(ioDispatcher) {
        storyDao.deleteStory(id)
    }
}