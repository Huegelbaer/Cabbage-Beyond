package com.cabbagebeyond.data.remote

import com.cabbagebeyond.data.dto.RoleDTO
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class RoleService {

    companion object {
        private const val COLLECTION_TITLE = RoleDTO.COLLECTION_TITLE
        private const val TAG = "RoleService"
    }

    suspend fun refreshRoles(): Result<Boolean> {
        var result = Result.success(true)
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.SERVER)
            .addOnSuccessListener {}
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun refreshRole(id: String): Result<Boolean> {
        var result = Result.success(true)
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.SERVER)
            .addOnSuccessListener { }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }
}