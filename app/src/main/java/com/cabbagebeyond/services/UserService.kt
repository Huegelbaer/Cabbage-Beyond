package com.cabbagebeyond.services

import com.cabbagebeyond.data.UserDataSource
import com.cabbagebeyond.model.User
import com.cabbagebeyond.util.Feature
import com.cabbagebeyond.util.FirebaseUtil
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.*
import org.koin.java.KoinJavaComponent.inject

class UserService {

    private val dataSource: UserDataSource by inject(UserDataSource::class.java)

    private val scope = CoroutineScope(Job() + Dispatchers.Default)

    private lateinit var _currentUser: User

    companion object {
        private const val TAG = "UserService"
        val instance = UserService()
        val currentUser: User
            get() = instance._currentUser
    }

    suspend fun updateUser(firebaseUser: FirebaseUser) {
        updateCurrentUser(firebaseUser)
    }

    private suspend fun loadUserFromFirebase() {
        val firebaseUser = FirebaseUtil.auth.currentUser
        firebaseUser?.let {
            updateCurrentUser(it)
        } ?: run {
            _currentUser = backupUser("")
        }
    }

    private suspend fun updateCurrentUser(user: FirebaseUser) {
        user.email?.let {
            _currentUser = loadCurrentUser(it)
        } ?: run {
            _currentUser = backupUser("")
        }
    }

    private suspend fun loadCurrentUser(email: String): User {
        val result = dataSource.getUserByEmail(email)
        return result.getOrNull() ?: backupUser(email)
    }

    private fun backupUser(email: String): User {
        return User(email, email, listOf(), listOf(), "")
    }

    fun userHasAccessToContent(): Boolean {
        return currentUser.features.containsAll(listOf(
            Feature.DETAIL_STORY_SAVAGE_WORLDS.value,
            Feature.DETAIL_SESSION_SAVAGE_WORLDS.value
        ))
    }

    fun userIsAdmin(): Boolean {
        return currentUser.features.contains(Feature.CONFIGURE_APP.value)
    }
}