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
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.app_ajudai.data.AuthResult
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onLogout: () -> Unit,
    onGoMyPosts: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    val currentUserId by authViewModel.currentUserId.collectAsState(initial = null)
    val userFlow = remember(currentUserId) { authViewModel.observeUser() ?: flowOf<User?>(null) }
    val user by userFlow.collectAsState(initial = null)

    var showNameDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Meu Perfil", style = MaterialTheme.typography.titleLarge) }) },
        snackbarHost = { SnackbarHost(hostState = snackbar) }
    ) { innerPadding ->
        if (user != null) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Avatar",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))
                Text(
                    text = user!!.name,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.width(8.dp))
                Text(
                    text = "4.5 / 5",
                    style = MaterialTheme.typography.bodyLarge
                )

                // ✅ Localidade do usuário
                Spacer(Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = user!!.location,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Ações
                Button(
                    onClick = onGoMyPosts,
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Minhas publicações") }

                Spacer(Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { showNameDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Editar nome") }

                OutlinedButton(
                    onClick = { showPasswordDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) { Text("Alterar senha") }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        authViewModel.logout()
                        onLogout()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Sair") }
            }
        } else {
            Box(
                Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) { Text("Faça login para ver seu perfil.") }
        }
    }

    // ===== Diálogo: Editar nome =====
    if (showNameDialog) {
        var newName by remember { mutableStateOf(user?.name ?: "") }
        AlertDialog(
            onDismissRequest = { showNameDialog = false },
            title = { Text("Editar nome") },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Novo nome") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        authViewModel.updateName(newName) {
                            scope.launch {
                                when (it) {
                                    is AuthResult.Success -> snackbar.showSnackbar("Nome atualizado!")
                                    is AuthResult.Error -> snackbar.showSnackbar(it.message)
                                }
                            }
                        }
                        showNameDialog = false
                    },
                    enabled = newName.isNotBlank()
                ) { Text("Salvar") }
            },
            dismissButton = {
                TextButton(onClick = { showNameDialog = false }) { Text("Cancelar") }
            }
        )
    }

    // ===== Diálogo: Alterar senha =====
    if (showPasswordDialog) {
        var oldPass by remember { mutableStateOf("") }
        var newPass by remember { mutableStateOf("") }
        var confirmNew by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text("Alterar senha") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = oldPass, onValueChange = { oldPass = it },
                        label = { Text("Senha atual") }, singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newPass, onValueChange = { newPass = it },
                        label = { Text("Nova senha") }, singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = confirmNew, onValueChange = { confirmNew = it },
                        label = { Text("Confirmar nova senha") }, singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                val valid = oldPass.isNotBlank() && newPass.isNotBlank() &&
                        confirmNew.isNotBlank() && (newPass == confirmNew)
                TextButton(
                    onClick = {
                        authViewModel.changePassword(oldPass, newPass) {
                            scope.launch {
                                when (it) {
                                    is AuthResult.Success -> snackbar.showSnackbar("Senha alterada!")
                                    is AuthResult.Error -> snackbar.showSnackbar(it.message)
                                }
                            }
                        }
                        showPasswordDialog = false
                    },
                    enabled = valid
                ) { Text("Salvar") }
            },
            dismissButton = {
                TextButton(onClick = { showPasswordDialog = false }) { Text("Cancelar") }
            }
        )
    }
}