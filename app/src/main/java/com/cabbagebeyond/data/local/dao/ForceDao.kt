package com.cabbagebeyond.data.local.dao

import android.util.Log
import com.cabbagebeyond.data.dto.ForceDTO
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class ForceDao {

    companion object {
        private const val COLLECTION_TITLE = ForceDTO.COLLECTION_TITLE
        private const val TAG = "ForceDao"
    }

    suspend fun getForces(): Result<List<ForceDTO>> {
        var result: Result<List<ForceDTO>> = Result.success(mutableListOf())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val forces = task.documents.mapNotNull { documentSnapshot ->
                    map(documentSnapshot)
                }
                result = Result.success(forces)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun getForces(ids: List<String>): Result<List<ForceDTO>> {
        var result: Result<List<ForceDTO>> = Result.success(mutableListOf())
        if (ids.isEmpty()) {
            return result
        }

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .whereIn(FieldPath.documentId(), ids)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val forces = task.documents.mapNotNull { documentSnapshot ->
                    map(documentSnapshot)
                }
                result = Result.success(forces)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun getForce(id: String): Result<ForceDTO> {
        var result: Result<ForceDTO> = Result.failure(Throwable())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val force = map(task)
                result = Result.success(force)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun saveForce(force: ForceDTO): Result<Boolean> {
        var result: Result<Boolean> = Result.success(true)
        val entity = force.toHashMap()

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(force.id)
            .set(entity)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { error ->
                Log.w(TAG, "Error writing document", error)
                result = Result.failure(error) }
            .await()
        return result
    }

    suspend fun deleteForce(id: String): Result<Boolean> {
        var result: Result<Boolean> = Result.success(true)

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

    private fun map(documentSnapshot: DocumentSnapshot): ForceDTO {
        return ForceDTO(
            extractString(ForceDTO.FIELD_NAME, documentSnapshot),
            extractString(ForceDTO.FIELD_DESCRIPTION, documentSnapshot),
            extractString(ForceDTO.FIELD_COST, documentSnapshot),
            extractString(ForceDTO.FIELD_DURATION, documentSnapshot),
            extractString(ForceDTO.FIELD_RANG_REQUIREMENT, documentSnapshot),
            extractString(ForceDTO.FIELD_RANGE, documentSnapshot),
            extractString(ForceDTO.FIELD_SHAPING, documentSnapshot),
            extractString(ForceDTO.FIELD_WORLD, documentSnapshot),
            documentSnapshot.id
        )
    }
}