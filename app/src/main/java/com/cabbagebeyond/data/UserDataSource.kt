package com.cabbagebeyond.data

import com.cabbagebeyond.data.dto.UserDTO

interface UserDataSource {

    //fun observeUsers(): LiveData<Result<List<UserDTO>>>

    suspend fun getUsers(): Result<List<UserDTO>>

    //suspend fun refreshUsers()

    //fun observeUser(id: String): LiveData<Result<UserDTO>>

    suspend fun getUser(id: String): Result<UserDTO>

    //suspend fun refreshUser(id: String)

    suspend fun saveUser(user: UserDTO)

    //suspend fun deleteAllUsers()

    suspend fun deleteUser(id: String)
}