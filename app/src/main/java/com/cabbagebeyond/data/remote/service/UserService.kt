package com.cabbagebeyond.data.remote.service

import com.cabbagebeyond.data.remote.dto.UserDTO
import com.cabbagebeyond.data.remote.extractListOfString
import com.cabbagebeyond.data.remote.extractString
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class UserService {

    companion object {
        private const val COLLECTION_TITLE = UserDTO.COLLECTION_TITLE
    }

    suspend fun refreshUsers(): Result<List<UserDTO>> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.isEmpty) {
            val users = querySnapshot.documents.map { map(it) }
            Result.success(users)
        } else {
            Result.failure(Throwable())
        }
    }

    suspend fun refreshUser(id: String): Result<UserDTO> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.exists()) {
            val user = map(querySnapshot)
            Result.success(user)
        } else {
            Result.failure(Throwable())
        }
    }

    private fun map(documentSnapshot: DocumentSnapshot): UserDTO {
        return UserDTO(
            extractString(UserDTO.FIELD_NAME, documentSnapshot),
            extractString(UserDTO.FIELD_EMAIL, documentSnapshot),
            extractListOfString(UserDTO.FIELD_FEATURES, documentSnapshot),
            extractListOfString(UserDTO.FIELD_ROLES, documentSnapshot),
            documentSnapshot.id
        )
    }
}