package com.cabbagebeyond.data.remote

import com.cabbagebeyond.data.dto.RoleDTO
import com.cabbagebeyond.data.local.dao.extractListOfString
import com.cabbagebeyond.data.local.dao.extractString
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class RoleService {

    companion object {
        private const val COLLECTION_TITLE = RoleDTO.COLLECTION_TITLE
    }

    suspend fun refreshRoles(): Result<List<RoleDTO>> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.isEmpty) {
            val roles = querySnapshot.documents.map { map(it) }
            Result.success(roles)
        } else {
            Result.failure(Throwable())
        }
    }

    suspend fun refreshRole(id: String): Result<RoleDTO> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.exists()) {
            val role = map(querySnapshot)
            Result.success(role)
        } else {
            Result.failure(Throwable())
        }
    }

    private fun map(documentSnapshot: DocumentSnapshot): RoleDTO {
        return RoleDTO(
            extractString(RoleDTO.FIELD_NAME, documentSnapshot),
            extractListOfString(RoleDTO.FIELD_FEATURES, documentSnapshot),
            documentSnapshot.id
        )
    }
}