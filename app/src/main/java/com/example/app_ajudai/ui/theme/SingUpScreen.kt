package com.example.app_ajudai.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.app_ajudai.AuthViewModel
import com.example.app_ajudai.data.AuthResult

@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel,
    onSuccessGoLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Criar Conta", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = name, onValueChange = { name = it },
            label = { Text("Nome") },
            singleLine = true, modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = location, onValueChange = { location = it },
            label = { Text("Local onde mora") },
            singleLine = true, modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("E-mail") },
            singleLine = true, modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Senha") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        if (error != null) {
            Spacer(Modifier.height(8.dp))
            Text(error!!, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                loading = true
                authViewModel.signUp(name, location, email, password) { res ->
                    loading = false
                    when (res) {
                        is AuthResult.Success -> onSuccessGoLogin()
                        is AuthResult.Error -> error = res.message
                    }
                }
            },
            enabled = !loading && name.isNotBlank() && location.isNotBlank() &&
                    email.isNotBlank() && password.isNotBlank(),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(if (loading) "Criando..." else "Criar Conta")
        }
    }
}