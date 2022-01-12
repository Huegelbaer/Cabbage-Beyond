package com.cabbagebeyond.data.dao

import android.util.Log
import com.cabbagebeyond.data.dto.CharacterDTO
import com.cabbagebeyond.util.FirebaseUtil
import kotlinx.coroutines.tasks.await

class CharacterDao {

    companion object {
        private const val COLLECTION_TITLE = CharacterDTO.COLLECTION_TITLE
        private const val TAG = "AbilityDao"
    }

    suspend fun getCharacters(): Result<List<CharacterDTO>> {
        var result: Result<List<CharacterDTO>> = Result.success(mutableListOf())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get()
            .addOnSuccessListener { task ->
                val characters = task.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(CharacterDTO::class.java)
                }
                result = Result.success(characters)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun getCharactersOfUser(id: String): Result<List<CharacterDTO>> {
        var result: Result<List<CharacterDTO>> = Result.success(mutableListOf())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .whereEqualTo(CharacterDTO.FIELD_OWNER, id)
            .get()
            .addOnSuccessListener { task ->
                val characters = task.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(CharacterDTO::class.java)
                }
                result = Result.success(characters)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun getCharacter(id: String): Result<CharacterDTO> {
        var result: Result<CharacterDTO> = Result.failure(Throwable())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get()
            .addOnSuccessListener { task ->
                task.toObject(CharacterDTO::class.java)?.let {
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

    suspend fun saveCharacter(character: CharacterDTO) {
        val entity = character.toHashMap()

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(character.id)
            .set(entity)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    suspend fun deleteCharacter(id: String) {
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }
}