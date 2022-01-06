package com.cabbagebeyond.data

import com.cabbagebeyond.model.User

interface UserDataSource {

    //fun observeUsers(): LiveData<Result<List<User>>>

    suspend fun getUsers(): Result<List<User>>

    //suspend fun refreshUsers()

    //fun observeUser(id: String): LiveData<Result<User>>

    suspend fun getUser(id: String): Result<User>

    //suspend fun refreshUser(id: String)

    suspend fun saveUser(user: User)

    //suspend fun deleteAllUsers()

    suspend fun deleteUser(id: String)
}