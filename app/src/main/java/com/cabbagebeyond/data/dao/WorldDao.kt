package com.cabbagebeyond.data.dao

import android.util.Log
import com.cabbagebeyond.data.dto.WorldDTO
import com.cabbagebeyond.util.FirebaseUtil
import kotlinx.coroutines.tasks.await

class WorldDao {

    companion object {
        private const val COLLECTION_TITLE = WorldDTO.COLLECTION_TITLE
        private const val TAG = "WorldDao"
    }

    suspend fun getWorlds(): Result<List<WorldDTO>> {
        var result: Result<List<WorldDTO>> = Result.success(mutableListOf())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get()
            .addOnSuccessListener { task ->
                val worlds = task.documents.mapNotNull { documentSnapshot ->
                  /*  val name = documentSnapshot.get(WorldDTO.FIELD_NAME, String::class.java)
                    val description = documentSnapshot.get(WorldDTO.FIELD_DESCRIPTION, String::class.java)
                    val rulebook = documentSnapshot.get(WorldDTO.FIELD_RULEBOOK, String::class.java)

                    WorldDTO(name!!, description, rulebook!!, documentSnapshot.id)
                    */
                    documentSnapshot.toObject(WorldDTO::class.java)
                }
                result = Result.success(worlds)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun getWorld(id: String): Result<WorldDTO> {
        var result: Result<WorldDTO> = Result.failure(Throwable())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get()
            .addOnSuccessListener { task ->
                task.toObject(WorldDTO::class.java)?.let {
                    result = Result.success(it)
                    return@addOnSuccessListener
                }
                result = Result.failure(Throwable())
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun saveWorld(world: WorldDTO) {
        val entity = world.toHashMap()

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(world.id)
            .set(entity)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    suspend fun deleteWorld(id: String) {
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }
}