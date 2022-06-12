package com.cabbagebeyond.data.dao

import android.content.res.Resources
import android.util.Log
import com.cabbagebeyond.data.dto.UserDTO
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class UserDao {

    companion object {
        private const val COLLECTION_TITLE = UserDTO.COLLECTION_TITLE
        private const val TAG = "UserDao"
    }

    suspend fun getUsers(): Result<List<UserDTO>> {
        var result: Result<List<UserDTO>> = Result.success(mutableListOf())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val users = task.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(UserDTO::class.java)
                }
                result = Result.success(users)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun getUser(id: String): Result<UserDTO> {
        var result: Result<UserDTO> = Result.failure(Throwable())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val user = map(task)
                result = Result.success(user)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun getUserByEmail(email: String): Result<UserDTO> {
        var result: Result<UserDTO> = Result.failure(Throwable())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .whereEqualTo(UserDTO.FIELD_EMAIL, email)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val user = task.documents.mapNotNull { documentSnapshot ->
                    map(documentSnapshot)
                }.firstOrNull()
                result = user?.let {
                    Result.success(it)
                } ?: Result.failure(Resources.NotFoundException())
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun saveUser(user: UserDTO) {
        val entity = user.toHashMap()

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(user.id)
            .set(entity)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
            .await()
    }

    suspend fun deleteUser(id: String) {
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
            .await()
    }

    private fun map(documentSnapshot: DocumentSnapshot): UserDTO {
        return UserDTO(
            extractString(UserDTO.FIELD_NAME, documentSnapshot),
            extractString(UserDTO.FIELD_EMAIL, documentSnapshot),
            extractListOfString(UserDTO.FIELD_FEATURES, documentSnapshot),
            extractListOfString(UserDTO.FIELD_ROLES, documentSnapshot),
            documentSnapshot.id
        )
    }
}