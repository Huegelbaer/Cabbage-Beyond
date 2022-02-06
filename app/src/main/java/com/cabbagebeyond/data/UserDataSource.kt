package com.cabbagebeyond.data

import com.cabbagebeyond.model.User

interface UserDataSource {

    //fun observeUsers(): LiveData<Result<List<User>>>

    //fun observeUser(id: String): LiveData<Result<User>>

    suspend fun getUsers(): Result<List<User>>

    suspend fun getUser(id: String): Result<User>

    suspend fun getUserByEmail(email: String): Result<User>

    suspend fun saveUser(user: User)

    suspend fun deleteUser(id: String)

    /*
        REMOTE
     */

    suspend fun refreshUsers(): Result<Boolean>

    suspend fun refreshUser(id: String): Result<Boolean>
}