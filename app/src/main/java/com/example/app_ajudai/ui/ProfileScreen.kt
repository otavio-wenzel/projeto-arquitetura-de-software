package com.example.app_ajudai.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.app_ajudai.R
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.flowOf
import androidx.compose.runtime.*
import com.example.app_ajudai.AuthViewModel
import com.example.app_ajudai.data.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(authViewModel: AuthViewModel) {
    // observa o id atual da sessão
    val currentUserId by authViewModel.currentUserId.collectAsState(initial = null)

    // pega um Flow<User?> válido (ou flowOf(null) se não houver usuário)
    val userFlow = remember(currentUserId) {
        authViewModel.observeUser() ?: flowOf<User?>(null)
    }

    // coleta o usuário (pode ser null)
    val user by userFlow.collectAsState(initial = null)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Meu Perfil", style = MaterialTheme.typography.titleLarge) }
            )
        }
    ) { innerPadding ->
        if (user != null) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(user!!.name, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(user!!.location, style = MaterialTheme.typography.bodyLarge)
                }
                Spacer(Modifier.height(24.dp))
                Button(onClick = { authViewModel.logout() }) { Text("Sair") }
            }
        } else {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Faça login para ver seu perfil.")
            }
        }
    }
}

@Composable
private fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}