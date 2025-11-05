package com.example.app_ajudai

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_ajudai.data.AuthResult
import com.example.app_ajudai.data.AppDatabase
import com.example.app_ajudai.data.UserRepositoryRoom
import com.example.app_ajudai.session.UserSession
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = UserRepositoryRoom(AppDatabase.get(app).userDao())
    private val session = UserSession(app)

    val currentUserId = session.userIdFlow.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    // ✅ chame isso no start da UI
    fun ensureValidSession() {
        viewModelScope.launch {
            val id = currentUserId.value
            if (id != null && !repo.userExists(id)) {
                session.setUserId(null) // limpa sessão “fantasma”
            }
        }
    }

    fun signUp(name: String, location: String, email: String, password: String, onDone: (AuthResult) -> Unit) {
        viewModelScope.launch {
            val res = repo.signUp(name, location, email, password)
            onDone(res)
        }
    }

    fun login(email: String, password: String, onDone: (AuthResult) -> Unit) {
        viewModelScope.launch {
            val (res, id) = repo.login(email, password)
            if (res is AuthResult.Success && id != null) {
                session.setUserId(id)
            }
            onDone(res)
        }
    }

    fun logout() {
        viewModelScope.launch { session.setUserId(null) }
    }

    fun observeUser() = currentUserId.value?.let { id ->
        repo.observeUser(id)
    }
}