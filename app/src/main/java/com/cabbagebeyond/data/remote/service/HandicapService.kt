package com.cabbagebeyond.data.remote.service

import com.cabbagebeyond.data.remote.dto.HandicapDTO
import com.cabbagebeyond.data.remote.extractString
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class HandicapService {

    companion object {
        private const val COLLECTION_TITLE = HandicapDTO.COLLECTION_TITLE
    }

    suspend fun refreshHandicaps(): Result<List<HandicapDTO>> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.isEmpty) {
            val handicaps = querySnapshot.documents.map { map(it) }
            Result.success(handicaps)
        } else {
            Result.failure(Throwable())
        }
    }

    suspend fun refreshHandicap(id: String): Result<HandicapDTO> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.exists()) {
            val handicap = map(querySnapshot)
            Result.success(handicap)
        } else {
            Result.failure(Throwable())
        }
    }

    private fun map(documentSnapshot: DocumentSnapshot): HandicapDTO {
        return HandicapDTO(
            extractString(HandicapDTO.FIELD_NAME, documentSnapshot),
            extractString(HandicapDTO.FIELD_DESCRIPTION, documentSnapshot),
            extractString(HandicapDTO.FIELD_TYPE, documentSnapshot),
            extractString(HandicapDTO.FIELD_WORLD, documentSnapshot),
            documentSnapshot.id
        )
    }
}