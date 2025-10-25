package com.example.app_ajudai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app_ajudai.ui.theme.AppajudaiTheme

/**
 * Tela de Detalhe do Favor (passo intermédio para o RF06)
 * @param favorId O ID do favor a ser exibido
 * @param onNavigateBack Ação para voltar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavorDetailScreen(
    favorId: String?,
    onNavigateBack: () -> Unit
    // TODO: Adicionar onChatClick: (String) -> Unit
) {
    // Procura o favor na nossa lista global
    val favor = mockFavores.find { it.id == favorId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhe do Favor", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        // Se o favor for encontrado, mostra os detalhes
        if (favor != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween // Empurra o botão para baixo
            ) {
                Column {
                    // Categoria
                    Text(
                        text = favor.categoria,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Título
                    Text(
                        text = favor.titulo,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Descrição Completa
                    Text(
                        text = favor.descricao,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                // Botão para iniciar o Chat (RF06)
                Button(
                    onClick = { /* TODO: Navegar para o ChatScreen */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Quero Ajudar!", style = MaterialTheme.typography.labelLarge)
                }
            }
        } else {
            // Se o favor não for encontrado (por segurança)
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                Text("Erro: Favor não encontrado.")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FavorDetailScreenPreview() {
    AppajudaiTheme {
        // Mostra o preview do primeiro favor da lista
        FavorDetailScreen(favorId = "1", onNavigateBack = {})
    }
}