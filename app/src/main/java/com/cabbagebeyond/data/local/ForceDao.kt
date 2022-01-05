package com.cabbagebeyond.data.local

import android.util.Log
import com.cabbagebeyond.data.dto.ForceDTO
import com.cabbagebeyond.util.FirebaseUtil
import kotlinx.coroutines.tasks.await

class ForceDao {

    companion object {
        private const val COLLECTION_TITLE = ForceDTO.COLLECTION_TITLE
        private const val TAG = "ForceDao"
    }

    suspend fun getForces(): Result<List<ForceDTO>> {
        var result: Result<List<ForceDTO>> = Result.success(mutableListOf())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get()
            .addOnSuccessListener { task ->
                val forces = task.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(ForceDTO::class.java)
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
            .get()
            .addOnSuccessListener { task ->
                task.toObject(ForceDTO::class.java)?.let {
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

    suspend fun saveForce(force: ForceDTO) {
        val entity = force.toHashMap()

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(force.id)
            .set(entity)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    suspend fun deleteForce(id: String) {
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }
}