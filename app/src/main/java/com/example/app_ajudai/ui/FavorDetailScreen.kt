package com.example.app_ajudai.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app_ajudai.data.Favor
import com.example.app_ajudai.data.FavorRepository
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavorDetailScreen(
    favorId: Long,
    repo: FavorRepository,
    onNavigateBack: () -> Unit,
    currentUserId: Long?
) {
    // 🔄 agora observamos Favor + User
    val favorWithUserFlow = remember(favorId) { repo.observarFavorComUsuario(favorId) }
    val favorWithUser by favorWithUserFlow.collectAsStateWithLifecycle(initialValue = null)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var alreadyNotified by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhe do Favor", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val data = favorWithUser
            if (data != null) {
                val isOwner = (currentUserId != null) && (currentUserId == data.favor.userId)

                FavorDetailContent(
                    favor = data.favor,
                    authorName = data.user.name,
                    showHelpButton = !isOwner,                   // 👈 só mostra se NÃO for dono
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    onHelpClick = {
                        val msg = if (!alreadyNotified) {
                            alreadyNotified = true
                            "Notificando o solicitante."
                        } else {
                            "O solicitante já foi notificado."
                        }
                        scope.launch { snackbarHostState.showSnackbar(msg) }
                    }
                )
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Erro: Favor não encontrado.", color = MaterialTheme.colorScheme.error)
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp)
            )
        }
    }
}

@Composable
private fun FavorDetailContent(
    favor: Favor,
    authorName: String,
    showHelpButton: Boolean,    // 👈 novo parâmetro
    modifier: Modifier = Modifier,
    onHelpClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // categoria
            Text(
                favor.categoria,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))

            // título
            Text(favor.titulo, style = MaterialTheme.typography.titleLarge)

            // 👇 bloco “Publicado por”
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Publicado por $authorName",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(16.dp))
            Text(favor.descricao, style = MaterialTheme.typography.bodyLarge)
        }

        if (showHelpButton) {
            Button(
                onClick = onHelpClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium
            ) { Text("Quero Ajudar!", style = MaterialTheme.typography.labelLarge) }
        } else {
            Text(
                "Esta é a sua publicação.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}