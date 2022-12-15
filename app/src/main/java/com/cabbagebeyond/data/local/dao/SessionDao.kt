package com.cabbagebeyond.data.local.dao

import android.util.Log
import com.cabbagebeyond.data.dto.SessionDTO
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
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
                    map(documentSnapshot)
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
                val session = map(task)
                Result.success(session)
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

    private fun map(documentSnapshot: DocumentSnapshot): SessionDTO {
        return SessionDTO(
            extractString(SessionDTO.FIELD_NAME, documentSnapshot),
            extractString(SessionDTO.FIELD_DESCRIPTION, documentSnapshot),
            extractString(SessionDTO.FIELD_PLAYER, documentSnapshot),
            extractString(SessionDTO.FIELD_STATUS, documentSnapshot),
            extractListOfString(SessionDTO.FIELD_INVITED_PLAYERS, documentSnapshot),
            extractString(SessionDTO.FIELD_OWNER, documentSnapshot),
            extractString(SessionDTO.FIELD_STORY, documentSnapshot),
            extractString(SessionDTO.FIELD_RULEBOOK, documentSnapshot),
            documentSnapshot.id
        )
    }
}