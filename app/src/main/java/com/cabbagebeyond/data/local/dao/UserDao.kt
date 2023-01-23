package com.cabbagebeyond.data.local.dao

import androidx.room.*
import com.cabbagebeyond.data.local.entities.UserEntity
import com.cabbagebeyond.data.local.relations.UserRoleCrossRef
import com.cabbagebeyond.data.local.relations.UserWithRoles

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    suspend fun getUsers(): List<UserWithRoles>

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUser(id: String): UserWithRoles

    @Query("SELECT * FROM user WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserWithRoles?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRoles(roles: List<UserRoleCrossRef>)

    @Delete
    suspend fun deleteUser(user: UserEntity)
}