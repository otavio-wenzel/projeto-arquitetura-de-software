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
    onNavigateBack: () -> Unit
) {
    val favorFlow = remember(favorId) { repo.observarPorId(favorId) }
    val favor by favorFlow.collectAsStateWithLifecycle(initialValue = null)

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
                .padding(innerPadding) // já considera a altura do TopAppBar
        ) {
            if (favor != null) {
                FavorDetailContent(
                    favor = favor!!,
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

            // ✅ Snackbar no topo
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.TopCenter) // topo da área já com padding do Scaffold
                    .padding(top = 12.dp)
            )
        }
    }
}

@Composable
private fun FavorDetailContent(
    favor: Favor,
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
            Text(
                favor.categoria,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(favor.titulo, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text(favor.descricao, style = MaterialTheme.typography.bodyLarge)
        }
        Button(
            onClick = onHelpClick,              // ✅ usa o callback
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Quero Ajudar!", style = MaterialTheme.typography.labelLarge)
        }
    }
}
