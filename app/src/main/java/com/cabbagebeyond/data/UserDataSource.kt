package com.cabbagebeyond.data

import com.cabbagebeyond.model.User

interface UserDataSource {

    suspend fun getUsers(): Result<List<User>>

    suspend fun getUser(id: String): Result<User>

    suspend fun getUserByEmail(email: String): Result<User?>

    suspend fun saveUser(user: User)

    suspend fun deleteUser(user: User)

    /*
        REMOTE
     */

    suspend fun refreshUsers()

    suspend fun refreshUser(id: String)
}