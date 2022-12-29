package com.cabbagebeyond.data.remote

import com.cabbagebeyond.data.dto.EquipmentDTO
import com.cabbagebeyond.data.local.dao.extractDouble
import com.cabbagebeyond.data.local.dao.extractString
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class EquipmentService {

    companion object {
        private const val COLLECTION_TITLE = EquipmentDTO.COLLECTION_TITLE
    }

    suspend fun refreshEquipments(): Result<List<EquipmentDTO>> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.isEmpty) {
            val equipments = querySnapshot.documents.map { map(it) }
            Result.success(equipments)
        } else {
            Result.failure(Throwable())
        }
    }

    suspend fun refreshEquipment(id: String): Result<EquipmentDTO> {
        val querySnapshot = FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.SERVER)
            .await()

        return if (!querySnapshot.exists()) {
            val equipment = map(querySnapshot)
            Result.success(equipment)
        } else {
            Result.failure(Throwable())
        }
    }

    private fun map(documentSnapshot: DocumentSnapshot): EquipmentDTO {
        return EquipmentDTO(
            extractString(EquipmentDTO.FIELD_NAME, documentSnapshot),
            extractString(EquipmentDTO.FIELD_DESCRIPTION, documentSnapshot),
            extractString(EquipmentDTO.FIELD_COST, documentSnapshot),
            extractDouble(EquipmentDTO.FIELD_WEIGHT, documentSnapshot),
            extractString(EquipmentDTO.FIELD_REQUIREMENTS, documentSnapshot),
            extractString(EquipmentDTO.FIELD_TYPE, documentSnapshot),
            extractString(EquipmentDTO.FIELD_WORLD, documentSnapshot),
            documentSnapshot.id
        )
    }
}