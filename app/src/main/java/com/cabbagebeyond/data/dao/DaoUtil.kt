package com.cabbagebeyond.data.dao

import com.google.firebase.firestore.DocumentSnapshot

fun extractInt(field: String, documentSnapshot: DocumentSnapshot): Int {
    return documentSnapshot.get(field, Int::class.java) ?: 0
}

fun extractDouble(field: String, documentSnapshot: DocumentSnapshot): Double {
    return documentSnapshot.get(field, Double::class.java) ?: 0.0
}

fun extractString(field: String, documentSnapshot: DocumentSnapshot): String {
    return documentSnapshot.get(field, String::class.java) ?: ""
}

fun extractListOfString(field: String, documentSnapshot: DocumentSnapshot): List<String> {
    return documentSnapshot.get(field) as? List<String> ?: listOf()
}