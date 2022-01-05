package com.cabbagebeyond.data.local

import android.util.Log
import com.cabbagebeyond.data.dto.EquipmentDTO
import com.cabbagebeyond.util.FirebaseUtil
import kotlinx.coroutines.tasks.await

class EquipmentDao {
    companion object {
        private const val COLLECTION_TITLE = EquipmentDTO.COLLECTION_TITLE
        private const val TAG = "EquipmentDao"
    }

    suspend fun getEquipments(): Result<List<EquipmentDTO>> {
        var result: Result<List<EquipmentDTO>> = Result.success(mutableListOf())
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get()
            .addOnSuccessListener { task ->
                val equipments = task.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(EquipmentDTO::class.java)
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
            .get()
            .addOnSuccessListener { task ->
                task.toObject(EquipmentDTO::class.java)?.let {
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

    suspend fun saveEquipment(equipment: EquipmentDTO) {
        val entity = equipment.toHashMap()

        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(equipment.id)
            .set(entity)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    suspend fun deleteEquipment(id: String) {
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }
}