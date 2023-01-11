package com.cabbagebeyond.data.remote

import com.cabbagebeyond.data.dto.StoryDTO
import com.cabbagebeyond.data.local.dao.extractString
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class StoryService {

    companion object {
        private const val COLLECTION_TITLE = StoryDTO.COLLECTION_TITLE
    }

    suspend fun refreshStories(): Result<List<StoryDTO>> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.isEmpty) {
            val stories = querySnapshot.documents.map { map(it) }
            Result.success(stories)
        } else {
            Result.failure(Throwable())
        }
    }

    suspend fun refreshStory(id: String): Result<StoryDTO> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.exists()) {
            val story = map(querySnapshot)
            Result.success(story)
        } else {
            Result.failure(Throwable())
        }
    }

    private fun map(documentSnapshot: DocumentSnapshot): StoryDTO {
        return StoryDTO(
            extractString(StoryDTO.FIELD_NAME, documentSnapshot),
            extractString(StoryDTO.FIELD_DESCRIPTION, documentSnapshot),
            extractString(StoryDTO.FIELD_STORY, documentSnapshot),
            extractString(StoryDTO.FIELD_OWNER, documentSnapshot),
            extractString(StoryDTO.FIELD_WORLD, documentSnapshot),
            extractString(StoryDTO.FIELD_RULEBOOK, documentSnapshot),
            documentSnapshot.id
        )
    }
}