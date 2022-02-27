package com.cabbagebeyond.data.dao

import android.util.Log
import com.cabbagebeyond.data.dto.SessionDTO
import com.cabbagebeyond.util.FirebaseUtil
import kotlinx.coroutines.tasks.await

class SessionDao {

    companion object {
        private const val COLLECTION_TITLE = SessionDTO.COLLECTION_TITLE
        private const val TAG = "SessionDao"
    }

    suspend fun getSessions(): Result<List<SessionDTO>> {
        var result: Result<List<SessionDTO>> = Result.success(mutableListOf())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get()
            .addOnSuccessListener { task ->
                val sessions = task.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(SessionDTO::class.java)
                }
                result = Result.success(sessions)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun getSession(id: String): Result<SessionDTO> {
        var result: Result<SessionDTO> = Result.failure(Throwable())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get()
            .addOnSuccessListener { task ->
                task.toObject(SessionDTO::class.java)?.let {
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

    fun saveSession(session: SessionDTO) {
        val entity = session.toHashMap()

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(session.id)
            .set(entity)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    fun deleteSession(id: String) {
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }
}