package com.example.app_ajudai

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app_ajudai.data.AppDatabase
import com.example.app_ajudai.data.FavorRepositoryRoom

class AppViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            val db = AppDatabase.get(app)
            val repo = FavorRepositoryRoom(db.favorDao())
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}