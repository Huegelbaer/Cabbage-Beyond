package com.cabbagebeyond.data.dao

import android.util.Log
import com.cabbagebeyond.data.dto.UserDTO
import com.cabbagebeyond.util.FirebaseUtil
import kotlinx.coroutines.tasks.await

class UserDao {

    companion object {
        private const val COLLECTION_TITLE = UserDTO.COLLECTION_TITLE
        private const val TAG = "UserDao"
    }

    suspend fun getUsers(): Result<List<UserDTO>> {
        var result: Result<List<UserDTO>> = Result.success(mutableListOf())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get()
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
            .get()
            .addOnSuccessListener { task ->
                task.toObject(UserDTO::class.java)?.let {
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

    suspend fun saveUser(user: UserDTO) {
        val entity = user.toHashMap()

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(user.id)
            .set(entity)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    suspend fun deleteUser(id: String) {
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }
}