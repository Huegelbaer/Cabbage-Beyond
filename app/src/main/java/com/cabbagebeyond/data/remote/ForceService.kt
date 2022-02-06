package com.cabbagebeyond.data.remote

import com.cabbagebeyond.data.dto.ForceDTO
import com.cabbagebeyond.util.FirebaseUtil
import kotlinx.coroutines.tasks.await

class ForceService {

    companion object {
        private const val COLLECTION_TITLE = ForceDTO.COLLECTION_TITLE
        private const val TAG = "ForceService"
    }

    suspend fun refreshForces(): Result<Boolean> {
        var result = Result.success(true)
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get()
            .addOnSuccessListener { }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun refreshForce(id: String): Result<Boolean> {
        var result = Result.success(true)
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get()
            .addOnSuccessListener { }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }
}