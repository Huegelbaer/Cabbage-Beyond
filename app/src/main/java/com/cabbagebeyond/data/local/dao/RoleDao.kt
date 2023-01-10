package com.cabbagebeyond.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import com.cabbagebeyond.data.local.entities.RoleEntity

@Dao
interface RoleDao {

    @Query("SELECT * FROM role")
    suspend fun getRoles(): List<RoleEntity>

    @Query("SELECT * FROM role WHERE id = :id")
    suspend fun getRole(id: String): RoleEntity

    @Update
    suspend fun saveRole(role: RoleEntity)

    @Delete
    suspend fun deleteRole(role: RoleEntity)
}