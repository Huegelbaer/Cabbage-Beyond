package com.cabbagebeyond.data.dao

import android.util.Log
import com.cabbagebeyond.data.dto.HandicapDTO
import com.cabbagebeyond.util.FirebaseUtil
import kotlinx.coroutines.tasks.await

class HandicapDao {

    companion object {
        private const val COLLECTION_TITLE = HandicapDTO.COLLECTION_TITLE
        private const val TAG = "HandicapDao"
    }

    suspend fun getHandicaps(): Result<List<HandicapDTO>> {
        var result: Result<List<HandicapDTO>> = Result.success(mutableListOf())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get()
            .addOnSuccessListener { task ->
                val handicaps = task.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(HandicapDTO::class.java)
                }
                result = Result.success(handicaps)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun getHandicap(id: String): Result<HandicapDTO> {
        var result: Result<HandicapDTO> = Result.failure(Throwable())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get()
            .addOnSuccessListener { task ->
                task.toObject(HandicapDTO::class.java)?.let {
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

    suspend fun saveHandicap(handicap: HandicapDTO) {
        val entity = handicap.toHashMap()

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(handicap.id)
            .set(entity)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    suspend fun deleteHandicap(id: String) {
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }
}