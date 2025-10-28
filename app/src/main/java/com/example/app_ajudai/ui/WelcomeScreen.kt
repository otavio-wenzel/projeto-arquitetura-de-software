package com.example.app_ajudai.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeScreen(
    onGoLogin: () -> Unit,
    onGoSignUp: () -> Unit
) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Ajudaí", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onGoLogin,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) { Text("Entrar") }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = onGoSignUp,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) { Text("Criar Conta") }
    }
}