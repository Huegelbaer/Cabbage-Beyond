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
            store.useEmulator("10.0.2.2", 8080)
        }
        return store
    }

    private fun setupAuth(): FirebaseAuth {
        val auth = FirebaseAuth.getInstance()
        if (USE_EMULATORS) {
            auth.useEmulator("10.0.2.2", 9099)
        }
        return auth
    }

    private fun setupAuthUI(): AuthUI {
        val ui = AuthUI.getInstance()
        if (USE_EMULATORS) {
            ui.useEmulator("10.0.2.2", 9099)
        }
        return ui
    }

    fun loginIntent(): Intent {
        val emailProvider = AuthUI.IdpConfig.EmailBuilder()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Workaround for PendingIntent missing MUTABLE FLAG crash
            emailProvider.setDefaultEmail("@")
        }

        val providers = mutableListOf(
            emailProvider.build()
        )

        return authUI
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.ic_launcher_foreground)
            .build()
    }

    fun isUserAlreadyLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun logLoginError(data: Intent?) {
        val response = IdpResponse.fromResultIntent(data)
        Log.i(TAG, "Sign in failed: ${response?.error?.errorCode}")
    }

    fun logout(context: Context, completeListener: OnCompleteListener<Void>) {
        authUI.signOut(context)
            .addOnCompleteListener(completeListener)
    }
}