package com.cabbagebeyond.data.dao

import android.util.Log
import com.cabbagebeyond.data.dto.RaceDTO
import com.cabbagebeyond.util.FirebaseUtil
import kotlinx.coroutines.tasks.await

class RaceDao {

    companion object {
        private const val COLLECTION_TITLE = RaceDTO.COLLECTION_TITLE
        private const val TAG = "RaceDao"
    }

    suspend fun getRaces(): Result<List<RaceDTO>> {
        var result: Result<List<RaceDTO>> = Result.success(mutableListOf())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get()
            .addOnSuccessListener { task ->
                val races = task.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(RaceDTO::class.java)
                }
                result = Result.success(races)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun getRace(id: String): Result<RaceDTO> {
        var result: Result<RaceDTO> = Result.failure(Throwable())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get()
            .addOnSuccessListener { task ->
                task.toObject(RaceDTO::class.java)?.let {
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

    suspend fun saveRace(race: RaceDTO) {
        val entity = race.toHashMap()

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(race.id)
            .set(entity)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    suspend fun deleteRace(id: String) {
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }
}