package com.cabbagebeyond.data.remote.service

import com.cabbagebeyond.data.remote.dto.ForceDTO
import com.cabbagebeyond.data.remote.extractString
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class ForceService {

    companion object {
        private const val COLLECTION_TITLE = ForceDTO.COLLECTION_TITLE
    }

    suspend fun refreshForces(): Result<List<ForceDTO>> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.isEmpty) {
            val forces = querySnapshot.documents.map { map(it) }
            Result.success(forces)
        } else {
            Result.failure(Throwable())
        }
    }

    suspend fun refreshForce(id: String): Result<ForceDTO> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.exists()) {
            val force = map(querySnapshot)
            Result.success(force)
        } else {
            Result.failure(Throwable())
        }
    }

    private fun map(documentSnapshot: DocumentSnapshot): ForceDTO {
        return ForceDTO(
            extractString(ForceDTO.FIELD_NAME, documentSnapshot),
            extractString(ForceDTO.FIELD_DESCRIPTION, documentSnapshot),
            extractString(ForceDTO.FIELD_COST, documentSnapshot),
            extractString(ForceDTO.FIELD_DURATION, documentSnapshot),
            extractString(ForceDTO.FIELD_RANG_REQUIREMENT, documentSnapshot),
            extractString(ForceDTO.FIELD_RANGE, documentSnapshot),
            extractString(ForceDTO.FIELD_SHAPING, documentSnapshot),
            extractString(ForceDTO.FIELD_WORLD, documentSnapshot),
            documentSnapshot.id
        )
    }
}