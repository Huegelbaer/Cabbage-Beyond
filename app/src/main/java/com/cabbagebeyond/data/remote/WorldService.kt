package com.cabbagebeyond.data.remote

import com.cabbagebeyond.data.dto.WorldDTO
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.firestore.Source

class WorldService {

    companion object {
        private const val COLLECTION_TITLE = WorldDTO.COLLECTION_TITLE
        private const val TAG = "WorldService"
    }

    fun refreshWorlds() {
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .get(Source.SERVER)
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }

    fun refreshWorld(id: String) {
        FirebaseUtil.firestore.collection(COLLECTION_TITLE)
            .document(id)
            .get(Source.SERVER)
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }
}