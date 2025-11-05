package com.example.app_ajudai.data

import kotlinx.coroutines.flow.Flow

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}

interface UserRepository {
    suspend fun signUp(name: String, location: String, email: String, password: String): AuthResult
    suspend fun login(email: String, password: String): Pair<AuthResult, Long?> // (resultado, userId)
    fun observeUser(userId: Long): Flow<User?>
    suspend fun userExists(id: Long): Boolean
}

class UserRepositoryRoom(private val dao: UserDao) : UserRepository {

    override suspend fun signUp(
        name: String,
        location: String,
        email: String,
        password: String
    ): AuthResult {
        if (name.isBlank() || location.isBlank() || email.isBlank() || password.isBlank()) {
            return AuthResult.Error("Preencha todos os campos.")
        }
        if (dao.getByEmail(email) != null) {
            return AuthResult.Error("E-mail já cadastrado.")
        }

        val hash = PasswordHasher.sha256(password)
        dao.insert(User(name = name, location = location, email = email, passwordHash = hash))
        return AuthResult.Success
    }

    override suspend fun login(email: String, password: String): Pair<AuthResult, Long?> {
        if (email.isBlank() || password.isBlank()) {
            return AuthResult.Error("Informe e-mail e senha.") to null
        }
        val user = dao.getByEmail(email) ?: return AuthResult.Error("Usuário não encontrado.") to null
        val hash = PasswordHasher.sha256(password)
        if (user.passwordHash != hash) return AuthResult.Error("Senha incorreta.") to null

        return AuthResult.Success to user.id
    }

    override fun observeUser(userId: Long): Flow<User?> = dao.observeById(userId)

    override suspend fun userExists(id: Long): Boolean = dao.existsById(id)
}