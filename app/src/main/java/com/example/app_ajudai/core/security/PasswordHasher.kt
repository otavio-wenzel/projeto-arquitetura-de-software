package com.example.app_ajudai.core.security

import java.security.MessageDigest

object PasswordHasher {
    fun sha256(text: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(text.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}