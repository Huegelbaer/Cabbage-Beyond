package com.cabbagebeyond

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoutingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Keep the splash screen visible for this Activity
        splashScreen.setKeepOnScreenCondition { true }

        if (AuthenticationService.isUserAlreadyLoggedIn()) {
            lifecycleScope.launch {
                updateUserAndNavigateIntoApp()
            }
        } else {
            navigateToAuthentication(this)
        }
    }

    private suspend fun updateUserAndNavigateIntoApp() = withContext(Dispatchers.IO) {
        UserService.instance.updateUser(FirebaseUtil.auth.currentUser!!)
        startRefreshWorker(applicationContext)
        withContext(Dispatchers.Main) {
            navigateIntoApp(this@RoutingActivity)
        }
    }
}