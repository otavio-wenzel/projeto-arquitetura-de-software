package com.example.app_ajudai.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): User?

    @Query("SELECT * FROM user WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<User?>
}