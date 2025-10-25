package com.example.app_ajudai

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons // <<< ESTA É A IMPORTAÇÃO QUE FALTAVA
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app_ajudai.ui.theme.AppajudaiTheme
import androidx.compose.material.icons.filled.Add



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    onAddFavorClick: () -> Unit,
    onFavorClick: (String) -> Unit
) {
    // A tela de Início agora mostra TODOS os favores, sem filtro
    val favores = mockFavores

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.ajudai_transparente),
                        contentDescription = "Logo Ajudaí",
                        modifier = Modifier.height(36.dp)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.shadow(elevation = 4.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddFavorClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                // Agora 'Icons.Filled.Add' será encontrado
                Icon(Icons.Filled.Add, contentDescription = "Solicitar Favor")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
        ) {
            items(favores) { favor ->
                // Usa o FavorCard do arquivo FavorCard.kt
                FavorCard(
                    favor = favor,
                    onClick = { onFavorClick(favor.id) }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FeedScreenPreview() {
    AppajudaiTheme {
        FeedScreen(onAddFavorClick = {}, onFavorClick = {})
    }
}