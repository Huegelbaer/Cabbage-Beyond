package com.cabbagebeyond.util

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.cabbagebeyond.BuildConfig
import com.cabbagebeyond.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

object FirebaseUtil {

    private const val TAG = "FirebaseUtil"
    private var USE_EMULATORS = BuildConfig.DEBUG

    val firestore: FirebaseFirestore by lazy {
        return@lazy setupFirestore()
    }

    val auth: FirebaseAuth by lazy {
        return@lazy setupAuth()
    }

    val authUI: AuthUI by lazy {
        return@lazy setupAuthUI()
    }

    private fun setupFirestore(): FirebaseFirestore {
        val store = FirebaseFirestore.getInstance()
        if (USE_EMULATORS) {
            store.useEmulator(BuildConfig.LOCAL_FIREBASE_URL, 8080)
        }

        val settings = FirebaseFirestoreSettings.Builder()
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        store.firestoreSettings = settings

        return store
    }

    private fun setupAuth(): FirebaseAuth {
        val auth = FirebaseAuth.getInstance()
        if (USE_EMULATORS) {
            auth.useEmulator(BuildConfig.LOCAL_FIREBASE_URL, 9099)
        }
        return auth
    }

    private fun setupAuthUI(): AuthUI {
        val ui = AuthUI.getInstance()
        if (USE_EMULATORS) {
            ui.useEmulator(BuildConfig.LOCAL_FIREBASE_URL, 9099)
        }
        return ui
    }
}