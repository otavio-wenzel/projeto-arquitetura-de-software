package com.example.app_ajudai.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app_ajudai.AppViewModel
import com.example.app_ajudai.categoriasDeFavor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    appViewModel: AppViewModel,
    onNavigateToFavorDetail: (Long) -> Unit
) {
    val selectedCategories by appViewModel.selectedCategories.collectAsStateWithLifecycle()
    val favores by appViewModel.favores.collectAsStateWithLifecycle()
    val searchQuery by appViewModel.searchQuery.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pesquisa e Filtros", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
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
                onValueChange = { appViewModel.setSearchQuery(it) },
                label = { Text("O que você procura?") },
                leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = "Pesquisar") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Column {
                Text("Filtrar por Categoria", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categoriasDeFavor.forEach { category ->
                        val isSelected = category in selectedCategories
                        FilterChip(
                            selected = isSelected,
                            onClick = { appViewModel.toggleCategory(category) },
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
                if (favores.isEmpty() && (selectedCategories.isNotEmpty() || searchQuery.isNotEmpty())) {
                    item {
                        Text(
                            text = "Nenhum favor encontrado com esses filtros.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
                items(favores, key = { it.id }) { favor ->
                    FavorCard(favor = favor) { onNavigateToFavorDetail(favor.id) }
                }
            }
        }
    }
}