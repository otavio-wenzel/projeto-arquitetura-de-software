package com.example.app_ajudai.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favor")
data class Favor(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titulo: String,
    val descricao: String,
    val categoria: String,
    val createdAt: Long = System.currentTimeMillis()
)