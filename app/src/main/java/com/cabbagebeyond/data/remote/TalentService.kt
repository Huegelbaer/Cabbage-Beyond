package com.cabbagebeyond.data.remote

import com.cabbagebeyond.data.dto.TalentDTO
import com.cabbagebeyond.data.local.dao.extractString
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class TalentService {

    companion object {
        private const val COLLECTION_TITLE = TalentDTO.COLLECTION_TITLE
    }

    suspend fun refreshTalents(): Result<List<TalentDTO>> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.isEmpty) {
            val talents = querySnapshot.documents.map { map(it) }
            Result.success(talents)
        } else {
            Result.failure(Throwable())
        }
    }

    suspend fun refreshTalent(id: String): Result<TalentDTO> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.exists()) {
            val talent = map(querySnapshot)
            Result.success(talent)
        } else {
            Result.failure(Throwable())
        }
    }

    private fun map(documentSnapshot: DocumentSnapshot): TalentDTO {
        return TalentDTO(
            extractString(TalentDTO.FIELD_NAME, documentSnapshot),
            extractString(TalentDTO.FIELD_DESCRIPTION, documentSnapshot),
            extractString(TalentDTO.FIELD_RANG_REQUIREMENT, documentSnapshot),
            extractString(TalentDTO.FIELD_REQUIREMENTS, documentSnapshot),
            extractString(TalentDTO.FIELD_TYPE, documentSnapshot),
            extractString(TalentDTO.FIELD_WORLD, documentSnapshot),
            documentSnapshot.id
        )
    }
}