package com.cabbagebeyond.data.dao

import android.util.Log
import com.cabbagebeyond.data.dto.RoleDTO
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class RoleDao {

    companion object {
        private const val COLLECTION_TITLE = RoleDTO.COLLECTION_TITLE
        private const val TAG = "RoleDao"
    }

    suspend fun getRoles(): Result<List<RoleDTO>> {
        var result: Result<List<RoleDTO>> = Result.success(mutableListOf())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val roles = task.documents.mapNotNull { documentSnapshot ->
                    map(documentSnapshot)
                }
                result = Result.success(roles)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun getRole(id: String): Result<RoleDTO> {
        var result: Result<RoleDTO> = Result.failure(Throwable())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val role = map(task)
                result = Result.success(role)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    fun saveRole(role: RoleDTO) {
        val entity = role.toHashMap()

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(role.id)
            .set(entity)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    fun deleteRole(id: String) {
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    private fun map(documentSnapshot: DocumentSnapshot): RoleDTO {
        return RoleDTO(
            extractString(RoleDTO.FIELD_NAME, documentSnapshot),
            extractListOfString(RoleDTO.FIELD_FEATURES, documentSnapshot),
            documentSnapshot.id
        )
    }
}