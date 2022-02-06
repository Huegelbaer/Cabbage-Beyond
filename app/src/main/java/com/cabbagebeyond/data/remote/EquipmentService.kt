package com.cabbagebeyond.data.remote

import com.cabbagebeyond.data.dto.EquipmentDTO
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class EquipmentService {

    companion object {
        private const val COLLECTION_TITLE = EquipmentDTO.COLLECTION_TITLE
        private const val TAG = "EquipmentService"
    }

    suspend fun refreshEquipments(): Result<Boolean> {
        var result = Result.success(true)
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.SERVER)
            .addOnSuccessListener { }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }

    suspend fun refreshEquipment(id: String): Result<Boolean> {
        var result = Result.success(true)
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.SERVER)
            .addOnSuccessListener { }
            .addOnFailureListener { exception ->
                result = Result.failure(exception.fillInStackTrace())
            }
            .await()
        return result
    }
}