package com.cabbagebeyond.data.remote

import com.cabbagebeyond.data.dto.RaceDTO
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class RaceService {

    companion object {
        private const val COLLECTION_TITLE = RaceDTO.COLLECTION_TITLE
        private const val TAG = "RaceService"
    }

    suspend fun refreshRaces(): Result<Boolean> {
        var result = Result.success(true)
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.SERVER)
            .addOnSuccessListener { }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun refreshRace(id: String): Result<Boolean> {
        var result = Result.success(true)
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.SERVER)
            .addOnSuccessListener { }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

}