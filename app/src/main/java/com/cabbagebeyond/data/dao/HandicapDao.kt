package com.cabbagebeyond.data.dao

import android.util.Log
import com.cabbagebeyond.data.dto.HandicapDTO
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class HandicapDao {

    companion object {
        private const val COLLECTION_TITLE = HandicapDTO.COLLECTION_TITLE
        private const val TAG = "HandicapDao"
    }

    suspend fun getHandicaps(): Result<List<HandicapDTO>> {
        var result: Result<List<HandicapDTO>> = Result.success(mutableListOf())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val handicaps = task.documents.mapNotNull { documentSnapshot ->
                    map(documentSnapshot)
                }
                result = Result.success(handicaps)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun getHandicaps(ids: List<String>): Result<List<HandicapDTO>> {
        var result: Result<List<HandicapDTO>> = Result.success(mutableListOf())
        if (ids.isEmpty()) {
            return result
        }

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .whereIn(FieldPath.documentId(), ids)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val handicaps = task.documents.mapNotNull { documentSnapshot ->
                    map(documentSnapshot)
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
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val handicap = map(task)
                result = Result.success(handicap)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun saveHandicap(handicap: HandicapDTO): Result<Boolean> {
        var result = Result.success(true)
        val entity = handicap.toHashMap()

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(handicap.id)
            .set(entity)
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

    suspend fun deleteHandicap(id: String): Result<Boolean> {
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

    private fun map(documentSnapshot: DocumentSnapshot): HandicapDTO {
        return HandicapDTO(
            extractString(HandicapDTO.FIELD_NAME, documentSnapshot),
            extractString(HandicapDTO.FIELD_DESCRIPTION, documentSnapshot),
            extractString(HandicapDTO.FIELD_TYPE, documentSnapshot),
            extractString(HandicapDTO.FIELD_WORLD, documentSnapshot),
            documentSnapshot.id
        )
    }
}