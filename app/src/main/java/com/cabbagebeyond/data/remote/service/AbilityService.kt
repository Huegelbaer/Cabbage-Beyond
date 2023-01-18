package com.cabbagebeyond.data.remote.service

import com.cabbagebeyond.data.remote.dto.AbilityDTO
import com.cabbagebeyond.data.remote.extractString
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class AbilityService {

    companion object {
        private const val COLLECTION_TITLE = AbilityDTO.COLLECTION_TITLE
    }

    suspend fun refreshAbilities(): Result<List<AbilityDTO>> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.isEmpty) {
            val abilities = querySnapshot.documents.map { map(it) }
            Result.success(abilities)
        } else {
            Result.failure(Throwable())
        }
    }

    suspend fun refreshAbility(id: String): Result<AbilityDTO> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.exists()) {
            val ability = map(querySnapshot)
            Result.success(ability)
        } else {
            Result.failure(Throwable())
        }
    }

    private fun map(documentSnapshot: DocumentSnapshot): AbilityDTO {
        return AbilityDTO(
            extractString(AbilityDTO.FIELD_NAME, documentSnapshot),
            extractString(AbilityDTO.FIELD_DESCRIPTION, documentSnapshot),
            extractString(AbilityDTO.FIELD_ATTRIBUTE, documentSnapshot),
            extractString(AbilityDTO.FIELD_WORLD, documentSnapshot),
            documentSnapshot.id
        )
    }
}