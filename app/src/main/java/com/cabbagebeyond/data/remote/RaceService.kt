package com.cabbagebeyond.data.remote

import com.cabbagebeyond.data.dto.RaceDTO
import com.cabbagebeyond.data.local.dao.extractString
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class RaceService {

    companion object {
        private const val COLLECTION_TITLE = RaceDTO.COLLECTION_TITLE
    }

    suspend fun refreshRaces(): Result<List<RaceDTO>> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.isEmpty) {
            val races = querySnapshot.documents.map { map(it) }
            Result.success(races)
        } else {
            Result.failure(Throwable())
        }
    }

    suspend fun refreshRace(id: String): Result<RaceDTO> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.exists()) {
            val race = map(querySnapshot)
            Result.success(race)
        } else {
            Result.failure(Throwable())
        }
    }

    private fun map(documentSnapshot: DocumentSnapshot): RaceDTO {

        val feats = documentSnapshot.get(RaceDTO.FIELD_RACE_FEATURES) as List<Map<String, String>>
        val features = feats.mapNotNull {
            it["id"]?.let { id ->
                RaceDTO.Feature(it["name"] ?: "", it["description"] ?: "", id)
            }
        }

        return RaceDTO(
            extractString(RaceDTO.FIELD_NAME, documentSnapshot),
            extractString(RaceDTO.FIELD_DESCRIPTION, documentSnapshot),
            features,
            extractString(RaceDTO.FIELD_WORLD, documentSnapshot),
            documentSnapshot.id
        )
    }
}