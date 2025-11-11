package com.example.app_ajudai.feature.auth.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user",
    indices = [Index(value = ["email"], unique = true)]
)
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val location: String,
    val email: String,
    val passwordHash: String,
    val createdAt: Long = System.currentTimeMillis()
)