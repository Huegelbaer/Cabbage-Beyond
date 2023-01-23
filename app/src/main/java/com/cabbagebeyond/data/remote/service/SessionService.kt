package com.cabbagebeyond.data.remote.service

import com.cabbagebeyond.data.remote.dto.SessionDTO
import com.cabbagebeyond.data.remote.extractListOfString
import com.cabbagebeyond.data.remote.extractString
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class SessionService {

    companion object {
        private const val COLLECTION_TITLE = SessionDTO.COLLECTION_TITLE
    }

    suspend fun refreshSessions(): Result<List<SessionDTO>> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.isEmpty) {
            val sessions = querySnapshot.documents.map { map(it) }
            Result.success(sessions)
        } else {
            Result.failure(Throwable())
        }
    }

    suspend fun refreshSession(id: String): Result<SessionDTO> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.exists()) {
            val session = map(querySnapshot)
            Result.success(session)
        } else {
            Result.failure(Throwable())
        }
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