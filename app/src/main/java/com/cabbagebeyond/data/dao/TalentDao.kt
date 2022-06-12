package com.cabbagebeyond.data.dao

import android.util.Log
import com.cabbagebeyond.data.dto.TalentDTO
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class TalentDao {

    companion object {
        private const val COLLECTION_TITLE = TalentDTO.COLLECTION_TITLE
        private const val TAG = "TalentDao"
    }

    suspend fun getTalents(): Result<List<TalentDTO>> {
        var result: Result<List<TalentDTO>> = Result.success(mutableListOf())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val talents = task.documents.mapNotNull { documentSnapshot ->
                    map(documentSnapshot)
                }
                result = Result.success(talents)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun getTalents(ids: List<String>): Result<List<TalentDTO>> {
        var result: Result<List<TalentDTO>> = Result.success(mutableListOf())
        if (ids.isEmpty()) {
            return result
        }

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .whereIn(FieldPath.documentId(), ids)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val talents = task.documents.mapNotNull { documentSnapshot ->
                    map(documentSnapshot)
                }
                result = Result.success(talents)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun getTalent(id: String): Result<TalentDTO> {
        var result: Result<TalentDTO> = Result.failure(Throwable())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val talent = map(task)
                result = Result.success(talent)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun saveTalent(talent: TalentDTO): Result<Boolean> {
        var result = Result.success(true)
        val entity = talent.toHashMap()

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(talent.id)
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

    suspend fun deleteTalent(id: String): Result<Boolean> {
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

    private fun map(documentSnapshot: DocumentSnapshot): TalentDTO {
        return TalentDTO(
            extractString(TalentDTO.FIELD_NAME, documentSnapshot),
            extractString(TalentDTO.FIELD_DESCRIPTION, documentSnapshot),
            extractString(TalentDTO.FIELD_RANG_REQUIREMENT, documentSnapshot),
            extractString(TalentDTO.FIELD_REQUIREMENTS, documentSnapshot),
            extractString(TalentDTO.FIELD_TYPE, documentSnapshot),
            extractString(TalentDTO.FIELD_WORLD, documentSnapshot),
            documentSnapshot.id
        )
    }
}