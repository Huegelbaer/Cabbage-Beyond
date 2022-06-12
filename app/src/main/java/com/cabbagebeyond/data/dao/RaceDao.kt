package com.cabbagebeyond.data.dao

import android.util.Log
import com.cabbagebeyond.data.dto.RaceDTO
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class RaceDao {

    companion object {
        private const val COLLECTION_TITLE = RaceDTO.COLLECTION_TITLE
        private const val TAG = "RaceDao"
    }

    suspend fun getRaces(): Result<List<RaceDTO>> {
        var result: Result<List<RaceDTO>> = Result.success(mutableListOf())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val races = task.documents.mapNotNull { documentSnapshot ->
                    map(documentSnapshot)
                }
                result = Result.success(races)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun getRace(id: String): Result<RaceDTO> {
        var result: Result<RaceDTO> = Result.failure(Throwable())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val race = map(task)
                result = Result.success(race)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun saveRace(race: RaceDTO): Result<Boolean> {
        var result = Result.success(true)
        val entity = race.toHashMap()

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(race.id)
            .set(entity)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { error ->
                Log.w(TAG, "Error writing document", error)
                result = Result.failure(error)
            }
            .await()
        return result
    }

    suspend fun deleteRace(id: String): Result<Boolean> {
        var result = Result.success(true)
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully deleted!")
            }
            .addOnFailureListener { error ->
                Log.w(TAG, "Error deleting document", error)
                result = Result.failure(error)
            }
            .await()
        return result
    }

    private fun map(documentSnapshot: DocumentSnapshot): RaceDTO {
        return RaceDTO(
            extractString(RaceDTO.FIELD_NAME, documentSnapshot),
            extractString(RaceDTO.FIELD_DESCRIPTION, documentSnapshot),
            extractListOfString(RaceDTO.FIELD_RACE_FEATURES, documentSnapshot),
            extractString(RaceDTO.FIELD_WORLD, documentSnapshot),
            documentSnapshot.id
        )
    }
}