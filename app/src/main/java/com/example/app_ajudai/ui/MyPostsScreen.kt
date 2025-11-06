package com.example.app_ajudai.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app_ajudai.AppViewModel
import com.example.app_ajudai.data.Favor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostsScreen(
    userId: Long,
    appViewModel: AppViewModel,
    onNavigateBack: () -> Unit,
    onOpenManage: (Long) -> Unit     // abrir tela de editar/excluir
) {
    val meusFavores by appViewModel.observarMeusFavores(userId).collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas publicações", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            if (meusFavores.isEmpty()) {
                item { Text("Você ainda não publicou nenhum favor.") }
            } else {
                items(meusFavores, key = { it.id }) { favor: Favor ->
                    FavorCard(favor = favor) { onOpenManage(favor.id) }
                }
            }
        }
    }
}
