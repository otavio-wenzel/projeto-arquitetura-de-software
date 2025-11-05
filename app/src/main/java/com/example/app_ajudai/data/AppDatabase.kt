package com.example.app_ajudai.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Favor::class, User::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favorDao(): FavorDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ajudai.db"
                )
                    // migração simples 1->2, se antes só tinha Favor
                    .fallbackToDestructiveMigration() // (rápido p/ projeto acadêmico; se quiser, crio Migration)
                    .build().also { INSTANCE = it }
            }
    }
}