package com.cabbagebeyond.data.remote

import com.cabbagebeyond.data.dto.WorldDTO
import com.cabbagebeyond.data.local.dao.extractString
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class WorldService {

    companion object {
        private const val COLLECTION_TITLE = WorldDTO.COLLECTION_TITLE
    }

    suspend fun refreshWorlds(): Result<List<WorldDTO>> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.isEmpty) {
            val worlds = querySnapshot.documents.map { map(it) }
            Result.success(worlds)
        } else {
            Result.failure(Throwable())
        }
    }

    suspend fun refreshWorld(id: String): Result<WorldDTO> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.exists()) {
            val world = map(querySnapshot)
            Result.success(world)
        } else {
            Result.failure(Throwable())
        }
    }

    private fun map(documentSnapshot: DocumentSnapshot): WorldDTO {
        return WorldDTO(
            extractString(WorldDTO.FIELD_NAME, documentSnapshot),
            extractString(WorldDTO.FIELD_DESCRIPTION, documentSnapshot),
            extractString(WorldDTO.FIELD_RULEBOOK, documentSnapshot),
            documentSnapshot.id
        )
    }
}