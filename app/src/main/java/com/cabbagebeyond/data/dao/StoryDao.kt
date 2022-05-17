package com.cabbagebeyond.data.dao

import android.util.Log
import com.cabbagebeyond.data.dto.StoryDTO
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await

class StoryDao {

    companion object {
        private const val COLLECTION_TITLE = StoryDTO.COLLECTION_TITLE
        private const val TAG = "StoryDao"
    }

    suspend fun getStories(): Result<List<StoryDTO>> {
        var result: Result<List<StoryDTO>> = Result.success(mutableListOf())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get()
            .addOnSuccessListener { task ->
                val stories = task.documents.mapNotNull { documentSnapshot ->
                    map(documentSnapshot)
                }
                result = Result.success(stories)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun getStory(id: String): Result<StoryDTO> {
        var result: Result<StoryDTO> = Result.failure(Throwable())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get()
            .addOnSuccessListener { task ->
                val story = map(task)
                result = Result.success(story)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    fun saveStory(story: StoryDTO) {
        val entity = story.toHashMap()

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(story.id)
            .set(entity)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    fun deleteStory(id: String) {
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    private fun map(documentSnapshot: DocumentSnapshot): StoryDTO {
        return StoryDTO(
            documentSnapshot.get(StoryDTO.FIELD_NAME, String::class.java) ?: "",
            documentSnapshot.get(StoryDTO.FIELD_DESCRIPTION, String::class.java) ?: "",
            documentSnapshot.get(StoryDTO.FIELD_STORY, String::class.java) ?: "",
            documentSnapshot.get(StoryDTO.FIELD_OWNER, String::class.java) ?: "",
            documentSnapshot.get(StoryDTO.FIELD_WORLD, String::class.java) ?: "",
            documentSnapshot.get(StoryDTO.FIELD_RULEBOOK, String::class.java) ?: "",
            documentSnapshot.id
        )
    }
}