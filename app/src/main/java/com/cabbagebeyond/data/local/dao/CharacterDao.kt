package com.cabbagebeyond.data.local.dao

import android.util.Log
import com.cabbagebeyond.data.dto.CharacterDTO
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
            .get(Source.CACHE)
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


    private suspend fun loadCharacters(query: Query): Result<List<CharacterDTO>> {
        var result: Result<List<CharacterDTO>> = Result.success(mutableListOf())
        query
            .get(Source.CACHE)
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
                .orderBy(fieldName)
        )
    }

    private fun map(documentSnapshot: DocumentSnapshot): CharacterDTO {
        return CharacterDTO(
            extractString(CharacterDTO.FIELD_NAME, documentSnapshot),
            extractString(CharacterDTO.FIELD_RACE, documentSnapshot),
            extractString(CharacterDTO.FIELD_DESCRIPTION, documentSnapshot),
            extractInt(CharacterDTO.FIELD_CHARISMA, documentSnapshot),
            extractString(CharacterDTO.FIELD_CONSTITUTION, documentSnapshot),
            extractString(CharacterDTO.FIELD_DECEPTION, documentSnapshot),
            extractString(CharacterDTO.FIELD_DEXTERITY, documentSnapshot),
            extractString(CharacterDTO.FIELD_INTELLIGENCE, documentSnapshot),
            extractString(CharacterDTO.FIELD_INVESTIGATION, documentSnapshot),
            extractString(CharacterDTO.FIELD_PERCEPTION, documentSnapshot),
            extractString(CharacterDTO.FIELD_STEALTH, documentSnapshot),
            extractString(CharacterDTO.FIELD_STRENGTH, documentSnapshot),
            extractString(CharacterDTO.FIELD_WILLPOWER, documentSnapshot),
            extractInt(CharacterDTO.FIELD_MOVEMENT, documentSnapshot),
            extractInt(CharacterDTO.FIELD_PARRY, documentSnapshot),
            extractString(CharacterDTO.FIELD_TOUGHNESS, documentSnapshot),
            extractListOfString(CharacterDTO.FIELD_ABILITIES, documentSnapshot),
            extractListOfString(CharacterDTO.FIELD_EQUIPMENTS, documentSnapshot),
            extractListOfString(CharacterDTO.FIELD_FORCES, documentSnapshot),
            extractListOfString(CharacterDTO.FIELD_HANDICAPS, documentSnapshot),
            extractListOfString(CharacterDTO.FIELD_TALENTS, documentSnapshot),
            extractString(CharacterDTO.FIELD_TYPE, documentSnapshot),
            extractString(CharacterDTO.FIELD_OWNER, documentSnapshot),
            extractString(CharacterDTO.FIELD_WORLD, documentSnapshot),
            documentSnapshot.id
        )
    }
}