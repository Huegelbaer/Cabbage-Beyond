package com.cabbagebeyond.data.local

import android.util.Log
import com.cabbagebeyond.data.dto.AbilityDTO
import com.cabbagebeyond.util.FirebaseUtil
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

    suspend fun saveAbility(ability: AbilityDTO) {
        val entity = ability.toHashMap()

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(ability.id)
            .set(entity)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    suspend fun deleteAbility(id: String) {
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }
}