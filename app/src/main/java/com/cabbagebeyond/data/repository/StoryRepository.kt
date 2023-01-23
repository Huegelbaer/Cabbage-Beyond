package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.StoryDataSource
import com.cabbagebeyond.data.remote.dto.StoryDTO
import com.cabbagebeyond.data.local.dao.StoryDao
import com.cabbagebeyond.data.local.entities.StoryEntity
import com.cabbagebeyond.data.local.relations.StoryOwnerCrossRef
import com.cabbagebeyond.data.local.relations.StoryWithEverything
import com.cabbagebeyond.data.local.relations.StoryWorldCrossRef
import com.cabbagebeyond.data.remote.service.StoryService
import com.cabbagebeyond.model.Story
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StoryRepository(
    private val storyDao: StoryDao,
    private val storyService: StoryService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : StoryDataSource {

    override suspend fun getStories(): Result<List<Story>> = withContext(ioDispatcher) {
        val result = storyDao.getStories()
        val list = result.map { it.asDomainModel() }
        return@withContext Result.success(list)
    }

    override suspend fun getStory(id: String): Result<Story> = withContext(ioDispatcher) {
        val result = storyDao.getStory(id)
        val list = result.asDomainModel()
        return@withContext Result.success(list)
    }

    override suspend fun saveStory(story: Story) = withContext(ioDispatcher) {
        storyDao.saveStory(story.asDatabaseModel())
    }

    override suspend fun deleteStory(story: Story) = withContext(ioDispatcher) {
        storyDao.deleteStory(story.asDatabaseModel())
    }


    override suspend fun refreshStories() = withContext(ioDispatcher) {
        val result = storyService.refreshStories()
        if (result.isSuccess) {
            result.getOrNull()?.forEach {
                save(it)
            }
        }
    }

    override suspend fun refreshStory(id: String) = withContext(ioDispatcher) {
        val result = storyService.refreshStory(id)
        if (result.isSuccess) {
            result.getOrNull()?.let {
                save(it)
            }
        }
    }

    private suspend fun save(story: StoryDTO) {
        val owner = StoryOwnerCrossRef(story.id, story.owner)
        storyDao.saveOwner(owner)
        val world = StoryWorldCrossRef(story.id, story.world)
        storyDao.saveWorld(world)
        storyDao.saveStory(story.asDatabaseModel())
    }
}

private fun List<StoryDTO>.asDatabaseModel(): List<StoryEntity> {
    return map {
        it.asDatabaseModel()
    }
}

private fun StoryDTO.asDatabaseModel(): StoryEntity {
    return StoryEntity(name, description, story, owner, world, id)
}

private fun Story.asDatabaseModel(): StoryEntity {
    return StoryEntity(name, description, story, owner.id, world?.id, id)
}

private fun StoryWithEverything.asDomainModel(): Story {
    return story.asDomainModel(owner.asDomainModel(listOf()), world.asDomainModel())
}

fun StoryEntity.asDomainModel(owner: User, world: World?): Story {
    return Story(name, description, story, owner, world, id)
}