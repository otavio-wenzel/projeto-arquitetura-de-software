package com.example.app_ajudai.data

import androidx.room.*
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Embedded
import androidx.room.Relation

@Entity(
    tableName = "favor",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE,   // se o usuário for removido, apaga seus favores
            onUpdate = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index("userId"),
        Index("categoria"),
        Index("createdAt")
    ]
)
data class Favor(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val titulo: String,
    val descricao: String,
    val categoria: String,
    val createdAt: Long = System.currentTimeMillis()
)
data class FavorWithUser(
    @Embedded val favor: Favor,
    @Relation(
        parentColumn = "userId",
        entityColumn = "id"
    )
    val user: User
)