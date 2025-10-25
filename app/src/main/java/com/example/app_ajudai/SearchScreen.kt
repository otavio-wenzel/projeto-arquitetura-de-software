package com.example.app_ajudai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_ajudai.ui.theme.AppajudaiTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    appViewModel: AppViewModel,
    onNavigateToFavorDetail: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val selectedCategories by appViewModel.selectedCategories.collectAsState()

    val favoresFiltrados = if (selectedCategories.isEmpty() && searchQuery.isEmpty()) {
        emptyList()
    } else {
        mockFavores.filter { favor ->
            val matchesCategory = if (selectedCategories.isEmpty()) {
                true
            } else {
                favor.categoria in selectedCategories
            }

            val matchesSearch = if (searchQuery.isEmpty()) {
                true
            } else {
                favor.titulo.contains(searchQuery, ignoreCase = true) ||
                        favor.descricao.contains(searchQuery, ignoreCase = true)
            }

            matchesCategory && matchesSearch
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Pesquisa e Filtros",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("O que você procura?") },
                leadingIcon = {
                    Icon(
                        // --- <<< CORREÇÃO 2: Use "Search" diretamente ---
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Ícone de Pesquisa"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Column {
                Text(
                    "Filtrar por Categoria",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categoriasDeFavor.forEach { category ->
                        val isSelected = selectedCategories.contains(category)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                appViewModel.toggleCategory(category)
                            },
                            label = { Text(category, style = MaterialTheme.typography.bodyMedium) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                if (favoresFiltrados.isEmpty() && (selectedCategories.isNotEmpty() || searchQuery.isNotEmpty())) {
                    item {
                        Text(
                            text = "Nenhum favor encontrado com esses filtros.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }

                items(favoresFiltrados) { favor ->
                    FavorCard(
                        favor = favor,
                        onClick = { onNavigateToFavorDetail(favor.id) }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    val fakeViewModel = AppViewModel()
    AppajudaiTheme {
        SearchScreen(
            appViewModel = fakeViewModel,
            onNavigateToFavorDetail = {}
        )
    }
}