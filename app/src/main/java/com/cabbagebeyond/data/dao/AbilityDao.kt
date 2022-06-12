package com.cabbagebeyond.data.dao

import android.util.Log
import com.cabbagebeyond.data.dto.AbilityDTO
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class AbilityDao {

    companion object {
        private const val COLLECTION_TITLE = AbilityDTO.COLLECTION_TITLE
        private const val TAG = "AbilityDao"
    }

    suspend fun getAbilities(): Result<List<AbilityDTO>> {
        var result: Result<List<AbilityDTO>> = Result.success(mutableListOf())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val abilities = task.documents.mapNotNull { documentSnapshot ->
                    map(documentSnapshot)
                }
                result = Result.success(abilities)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun getAbilities(ids: List<String>): Result<List<AbilityDTO>> {
        var result: Result<List<AbilityDTO>> = Result.success(mutableListOf())
        if (ids.isEmpty()) {
            return result
        }

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .whereIn(FieldPath.documentId(), ids)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val abilities = task.documents.mapNotNull { documentSnapshot ->
                    map(documentSnapshot)
                }
                result = Result.success(abilities)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun getAbility(id: String): Result<AbilityDTO> {
        var result: Result<AbilityDTO> = Result.failure(Throwable())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val ability = map(task)
                result = Result.success(ability)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun saveAbility(ability: AbilityDTO): Result<Boolean> {
        var result: Result<Boolean> = Result.failure(Throwable())
        val entity = ability.toHashMap()

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(ability.id)
            .set(entity)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
                result = Result.success(true)
            }
            .addOnFailureListener { error ->
                Log.w(TAG, "Error writing document", error)
                result = Result.failure(error)
            }
            .await()

        return result
    }

    suspend fun deleteAbility(id: String): Result<Boolean> {
        var result: Result<Boolean> = Result.failure(Throwable())

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully deleted!")
                result = Result.success(true)
            }
            .addOnFailureListener { error ->
                Log.w(TAG, "Error deleting document", error)
                result = Result.failure(error)
            }
            .await()

        return result
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