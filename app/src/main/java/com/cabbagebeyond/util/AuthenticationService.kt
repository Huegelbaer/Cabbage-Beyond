package com.cabbagebeyond.util

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.cabbagebeyond.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.OnCompleteListener

object AuthenticationService {

    private const val TAG = "AuthenticationService"

    fun loginIntent(): Intent {
        val emailProvider = AuthUI.IdpConfig.EmailBuilder()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Workaround for PendingIntent missing MUTABLE FLAG crash
            emailProvider.setDefaultEmail("@")
        }

        val providers = mutableListOf(
            emailProvider.build()
        )

        return FirebaseUtil.authUI
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.ic_launcher_foreground)
            .build()
    }

    fun isUserAlreadyLoggedIn(): Boolean {
        return FirebaseUtil.auth.currentUser != null
    }

    fun logLoginError(data: Intent?) {
        val response = IdpResponse.fromResultIntent(data)
        Log.i(TAG, "Sign in failed: ${response?.error?.errorCode}")
    }

    fun logout(context: Context, completeListener: OnCompleteListener<Void>) {
        FirebaseUtil.authUI.signOut(context)
            .addOnCompleteListener(completeListener)
    }
}