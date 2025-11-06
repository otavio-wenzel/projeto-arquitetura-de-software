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

    @Query("SELECT EXISTS(SELECT 1 FROM user WHERE id = :id)")
    suspend fun existsById(id: Long): Boolean

    @Query("SELECT * FROM user WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): User?

    @Query("UPDATE user SET name = :name WHERE id = :id")
    suspend fun updateName(id: Long, name: String): Int

    @Query("UPDATE user SET passwordHash = :passwordHash WHERE id = :id")
    suspend fun updatePasswordHash(id: Long, passwordHash: String): Int

}