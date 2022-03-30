package com.cabbagebeyond.ui.auth

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.cabbagebeyond.databinding.ActivityAuthenticationBinding
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.ui.onboarding.OnboardingFragment
import com.cabbagebeyond.util.AuthenticationService
import com.cabbagebeyond.util.FirebaseUtil
import com.cabbagebeyond.util.navigateIntoApp
import com.cabbagebeyond.util.startRefreshWorker
import kotlinx.coroutines.launch

class AuthenticationActivity : AppCompatActivity() {

    companion object {
        private const val SIGN_IN_REQUEST_CODE = 1001
    }

    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthenticationBinding.inflate(layoutInflater)

        binding.loginButton.setOnClickListener {
            startActivityForResult(AuthenticationService.loginIntent(), SIGN_IN_REQUEST_CODE)
        }

        if (AuthenticationService.isUserAlreadyLoggedIn()) {
            lifecycleScope.launch {
                updateUserAndNavigateIntoApp()
            }
        } else {
            setContentView(binding.root)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                updateUserAndShowOnboarding()
            } else {
                AuthenticationService.logLoginError(data)
            }
        }
    }

    private fun updateUserAndNavigateIntoApp() {
        lifecycleScope.launch {
            updateUser()
        }
        navigateIntoApp(this)
    }

    private suspend fun updateUser() {
        UserService.instance.updateUser(FirebaseUtil.auth.currentUser!!)
        startRefreshWorker(applicationContext)
    }

    private fun updateUserAndShowOnboarding() {
        lifecycleScope.launch {
            updateUser()
        }
        showOnboarding()
    }

    private fun showOnboarding() {
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, OnboardingFragment())
            .commit()
    }
}