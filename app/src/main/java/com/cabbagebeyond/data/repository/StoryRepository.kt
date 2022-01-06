package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.StoryDataSource
import com.cabbagebeyond.data.dto.StoryDTO
import com.cabbagebeyond.data.dao.StoryDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StoryRepository(
    private val storyDao: StoryDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : StoryDataSource {

    override suspend fun getStories(): Result<List<StoryDTO>> = withContext(ioDispatcher) {
        return@withContext storyDao.getStories()
    }

    override suspend fun getStory(id: String): Result<StoryDTO> = withContext(ioDispatcher) {
        return@withContext storyDao.getStory(id)
    }

    override suspend fun saveStory(story: StoryDTO) = withContext(ioDispatcher) {
        storyDao.saveStory(story)
    }

    override suspend fun deleteStory(id: String) = withContext(ioDispatcher) {
        storyDao.deleteStory(id)
    }
}