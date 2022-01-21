package com.cabbagebeyond.data.dao

import android.util.Log
import com.cabbagebeyond.data.dto.AbilityDTO
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.FieldPath
import kotlinx.coroutines.tasks.await

class AbilityDao {

    companion object {
        private const val COLLECTION_TITLE = AbilityDTO.COLLECTION_TITLE
        private const val TAG = "AbilityDao"
    }

    suspend fun getAbilities(): Result<List<AbilityDTO>> {
        var result: Result<List<AbilityDTO>> = Result.success(mutableListOf())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get()
            .addOnSuccessListener { task ->
                val abilities = task.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(AbilityDTO::class.java)
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
            .get()
            .addOnSuccessListener { task ->
                val abilities = task.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(AbilityDTO::class.java)
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
            .get()
            .addOnSuccessListener { task ->
                task.toObject(AbilityDTO::class.java)?.let {
                    result = Result.success(it)
                    return@addOnSuccessListener
                }
                result = Result.failure(Throwable())
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
}