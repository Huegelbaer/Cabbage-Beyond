package com.cabbagebeyond.data.dao

import android.util.Log
import com.cabbagebeyond.data.dto.CharacterDTO
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await

class CharacterDao {

    companion object {
        private const val COLLECTION_TITLE = CharacterDTO.COLLECTION_TITLE
        private const val TAG = "CharacterDao"
    }

    suspend fun getCharacters(): Result<List<CharacterDTO>> {
        var result: Result<List<CharacterDTO>> = Result.success(mutableListOf())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get()
            .addOnSuccessListener { task ->
                val characters = task.documents.mapNotNull { documentSnapshot ->
                    map(documentSnapshot)
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
                    map(documentSnapshot)
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
                val character = map(task)
                result = Result.success(character)
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

    private fun map(documentSnapshot: DocumentSnapshot): CharacterDTO {
        return CharacterDTO(
        documentSnapshot.get(CharacterDTO.FIELD_NAME, String::class.java) ?: "",
        documentSnapshot.get(CharacterDTO.FIELD_DESCRIPTION, String::class.java) ?: "",
        documentSnapshot.get(CharacterDTO.FIELD_CHARISMA, Int::class.java) ?: 0,
        documentSnapshot.get(CharacterDTO.FIELD_CONSTITUTION, String::class.java) ?: "",
        documentSnapshot.get(CharacterDTO.FIELD_DECEPTION, String::class.java) ?: "",
        documentSnapshot.get(CharacterDTO.FIELD_DEXTERITY, String::class.java) ?: "",
        documentSnapshot.get(CharacterDTO.FIELD_INTELLIGENCE, String::class.java) ?: "",
        documentSnapshot.get(CharacterDTO.FIELD_INVESTIGATION, String::class.java) ?: "",
        documentSnapshot.get(CharacterDTO.FIELD_PERCEPTION, String::class.java) ?: "",
        documentSnapshot.get(CharacterDTO.FIELD_STEALTH, String::class.java) ?: "",
        documentSnapshot.get(CharacterDTO.FIELD_STRENGTH, String::class.java) ?: "",
        documentSnapshot.get(CharacterDTO.FIELD_WILLPOWER, String::class.java) ?: "",
        documentSnapshot.get(CharacterDTO.FIELD_MOVEMENT, Int::class.java) ?: 0,
        documentSnapshot.get(CharacterDTO.FIELD_PARRY, Int::class.java) ?: 0,
        documentSnapshot.get(CharacterDTO.FIELD_TOUGHNESS, String::class.java) ?: "",
        documentSnapshot.get(CharacterDTO.FIELD_ABILITIES) as List<String>,
        documentSnapshot.get(CharacterDTO.FIELD_EQUIPMENTS) as List<String>,
        documentSnapshot.get(CharacterDTO.FIELD_FORCES) as List<String>,
        documentSnapshot.get(CharacterDTO.FIELD_HANDICAPS) as List<String>,
        documentSnapshot.get(CharacterDTO.FIELD_TALENTS) as List<String>,
        documentSnapshot.get(CharacterDTO.FIELD_TYPE, String::class.java) ?: "",
        documentSnapshot.get(CharacterDTO.FIELD_OWNER, String::class.java) ?: "",
        documentSnapshot.get(CharacterDTO.FIELD_WORLD, String::class.java) ?: "",
            documentSnapshot.id
        )
    }
}