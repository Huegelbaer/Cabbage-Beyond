package com.cabbagebeyond.ui.auth

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.cabbagebeyond.MainActivity
import com.cabbagebeyond.databinding.ActivityAuthenticationBinding
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.util.AuthenticationService
import com.cabbagebeyond.util.FirebaseUtil
import com.cabbagebeyond.util.startRefreshWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationActivity : AppCompatActivity() {

    companion object {
        private const val SIGN_IN_REQUEST_CODE = 1001
    }

    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            startActivityForResult(AuthenticationService.loginIntent(), SIGN_IN_REQUEST_CODE)
        }

        if (AuthenticationService.isUserAlreadyLoggedIn()) {
            lifecycleScope.launch {
                updateUserAndNavigateIntoApp()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                lifecycleScope.launch {
                    updateUserAndNavigateIntoApp()
                }
            } else {
                AuthenticationService.logLoginError(data)
            }
        }
    }

    private suspend fun updateUserAndNavigateIntoApp() {
        UserService.instance.updateUser(FirebaseUtil.auth.currentUser!!)
        startRefreshWorker(applicationContext)
        withContext(Dispatchers.Main) {
            navigateIntoApp()
        }
    }

    private fun navigateIntoApp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
        startActivity(intent)
        finish()
    }
}