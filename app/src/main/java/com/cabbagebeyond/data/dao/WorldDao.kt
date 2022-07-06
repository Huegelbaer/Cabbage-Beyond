package com.cabbagebeyond.data.dao

import android.util.Log
import com.cabbagebeyond.data.dto.WorldDTO
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class WorldDao {

    companion object {
        private const val COLLECTION_TITLE = WorldDTO.COLLECTION_TITLE
        private const val TAG = "WorldDao"
    }

    suspend fun getWorlds(): Result<List<WorldDTO>> {
        val result: Result<List<WorldDTO>>
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.CACHE)
            .await()

        val worlds = querySnapshot.documents.mapNotNull { documentSnapshot ->
            map(documentSnapshot)
        }
        result = if (worlds.isNullOrEmpty()) Result.failure(Exception()) else Result.success(worlds)
        return result
    }

    suspend fun getWorld(id: String): Result<WorldDTO> {
        var result: Result<WorldDTO> = Result.failure(Throwable())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val world = map(task)
                result = Result.success(world)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun saveWorld(world: WorldDTO): Result<Boolean> {
        var result = Result.success(true)
        val entity = world.toHashMap()

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(world.id)
            .set(entity, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { error ->
                Log.w(TAG, "Error writing document", error)
                result = Result.failure(error)
            }
            .await()
        return result
    }

    suspend fun deleteWorld(id: String): Result<Boolean> {
        var result = Result.success(true)
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully deleted!")
            }
            .addOnFailureListener { error ->
                Log.w(TAG, "Error deleting document", error)
                result = Result.failure(error)
            }
            .await()
        return result
    }

    private fun map(documentSnapshot: DocumentSnapshot): WorldDTO {
        return WorldDTO(
            extractString(WorldDTO.FIELD_NAME, documentSnapshot),
            extractString(WorldDTO.FIELD_DESCRIPTION, documentSnapshot),
            extractString(WorldDTO.FIELD_RULEBOOK, documentSnapshot),
            documentSnapshot.id
        )
    }
}