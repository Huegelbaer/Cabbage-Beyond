package com.cabbagebeyond.data.dao

import android.util.Log
import com.cabbagebeyond.data.dto.CharacterDTO
import com.cabbagebeyond.model.Character
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class CharacterDao {

    companion object {
        private const val COLLECTION_TITLE = CharacterDTO.COLLECTION_TITLE
        private const val TAG = "CharacterDao"
    }

    suspend fun getCharacters(): Result<List<CharacterDTO>> {
        return loadCharacters(
            FirebaseUtil.firestore.collection(COLLECTION_TITLE)
        )
    }

    suspend fun getCharactersOrderedByName(): Result<List<CharacterDTO>> =
        getCharactersOrdered(CharacterDTO.FIELD_NAME)

    suspend fun getCharactersOrderedByRace(): Result<List<CharacterDTO>> =
        getCharactersOrdered(CharacterDTO.FIELD_RACE)

    suspend fun getCharactersOrderedByType(): Result<List<CharacterDTO>> =
        getCharactersOrdered(CharacterDTO.FIELD_TYPE)

    suspend fun getCharactersOrderedByWorld(): Result<List<CharacterDTO>> =
        getCharactersOrdered(CharacterDTO.FIELD_WORLD)

    suspend fun getCharactersOfUser(id: String): Result<List<CharacterDTO>> {
        return loadCharacters(
            FirebaseUtil.firestore.collection(COLLECTION_TITLE)
                .whereEqualTo(CharacterDTO.FIELD_OWNER, id)
        )
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

    suspend fun saveCharacter(character: CharacterDTO): Result<Boolean> {
        var result: Result<Boolean> = Result.success(true)
        val entity = character.toHashMap()

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(character.id)
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

    suspend fun deleteCharacter(id: String): Result<Boolean> {
        var result: Result<Boolean> = Result.success(true)

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


    private suspend fun loadCharacters(
        query: Query,
        source: Source = Source.DEFAULT
    ): Result<List<CharacterDTO>> {
        var result: Result<List<CharacterDTO>> = Result.success(mutableListOf())
        query
            .get(source)
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

    private suspend fun getCharactersOrdered(fieldName: String): Result<List<CharacterDTO>> {
        return loadCharacters(
            FirebaseUtil.firestore.collection(COLLECTION_TITLE)
                .orderBy(fieldName),
            Source.CACHE
        )
    }

    private fun map(documentSnapshot: DocumentSnapshot): CharacterDTO {
        return CharacterDTO(
            documentSnapshot.get(CharacterDTO.FIELD_NAME, String::class.java) ?: "",
            documentSnapshot.get(CharacterDTO.FIELD_RACE, String::class.java) ?: "",
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