package com.cabbagebeyond.data.dao

import android.util.Log
import com.cabbagebeyond.data.dto.EquipmentDTO
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class EquipmentDao {

    companion object {
        private const val COLLECTION_TITLE = EquipmentDTO.COLLECTION_TITLE
        private const val TAG = "EquipmentDao"
    }

    suspend fun getEquipments(): Result<List<EquipmentDTO>> {
        var result: Result<List<EquipmentDTO>> = Result.success(mutableListOf())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val equipments = task.documents.mapNotNull { documentSnapshot ->
                    map(documentSnapshot)
                }
                result = Result.success(equipments)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun getEquipments(ids: List<String>): Result<List<EquipmentDTO>> {
        var result: Result<List<EquipmentDTO>> = Result.success(mutableListOf())
        if (ids.isEmpty()) {
            return result
        }

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .whereIn(FieldPath.documentId(), ids)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val equipments = task.documents.mapNotNull { documentSnapshot ->
                    map(documentSnapshot)
                }
                result = Result.success(equipments)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun getEquipment(id: String): Result<EquipmentDTO> {
        var result: Result<EquipmentDTO> = Result.failure(Throwable())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.CACHE)
            .addOnSuccessListener { task ->
                val equipment = map(task)
                result = Result.success(equipment)
            }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun saveEquipment(equipment: EquipmentDTO): Result<Boolean> {
        var result: Result<Boolean> = Result.failure(Throwable())

        val entity = equipment.toHashMap()

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(equipment.id)
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

    suspend fun deleteEquipment(id: String): Result<Boolean> {
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

    private fun map(documentSnapshot: DocumentSnapshot): EquipmentDTO {
        return EquipmentDTO(
            documentSnapshot.get(EquipmentDTO.FIELD_NAME, String::class.java) ?: "",
            documentSnapshot.get(EquipmentDTO.FIELD_DESCRIPTION, String::class.java) ?: "",
            documentSnapshot.get(EquipmentDTO.FIELD_COST, String::class.java) ?: "",
            documentSnapshot.get(EquipmentDTO.FIELD_WEIGHT, Double::class.java) ?: 0.0,
            documentSnapshot.get(EquipmentDTO.FIELD_REQUIREMENTS, String::class.java) ?: "",
            documentSnapshot.get(EquipmentDTO.FIELD_TYPE, String::class.java) ?: "",
            documentSnapshot.get(EquipmentDTO.FIELD_WORLD, String::class.java) ?: "",
            documentSnapshot.id
        )
    }
}