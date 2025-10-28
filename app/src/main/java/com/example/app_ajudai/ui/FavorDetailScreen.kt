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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavorDetailScreen(
    favorId: Long,
    repo: FavorRepository,
    onNavigateBack: () -> Unit
) {
    val favorFlow = remember(favorId) { repo.observarPorId(favorId) }
    val favor by favorFlow.collectAsStateWithLifecycle(initialValue = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhe do Favor", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        if (favor != null) {
            FavorDetailContent(favor!!)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) { Text("Erro: Favor não encontrado.", color = MaterialTheme.colorScheme.error) }
        }
    }
}

@Composable
private fun FavorDetailContent(favor: Favor) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(favor.categoria, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(favor.titulo, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text(favor.descricao, style = MaterialTheme.typography.bodyLarge)
        }
        Button(
            onClick = { /* TODO: Chat */ },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = MaterialTheme.shapes.medium
        ) { Text("Quero Ajudar!", style = MaterialTheme.typography.labelLarge) }
    }
}