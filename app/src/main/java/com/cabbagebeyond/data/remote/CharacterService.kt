package com.cabbagebeyond.data.remote

import com.cabbagebeyond.data.dto.CharacterDTO
import com.cabbagebeyond.data.local.dao.extractInt
import com.cabbagebeyond.data.local.dao.extractListOfString
import com.cabbagebeyond.data.local.dao.extractString
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class CharacterService {

    companion object {
        private const val COLLECTION_TITLE = CharacterDTO.COLLECTION_TITLE
    }

    suspend fun refreshCharacters(): Result<List<CharacterDTO>> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.isEmpty) {
            val characters = querySnapshot.documents.map { map(it) }
            Result.success(characters)
        } else {
            Result.failure(Throwable())
        }
    }

    suspend fun refreshCharacter(id: String): Result<CharacterDTO> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.exists()) {
            val character = map(querySnapshot)
            Result.success(character)
        } else {
            Result.failure(Throwable())
        }
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