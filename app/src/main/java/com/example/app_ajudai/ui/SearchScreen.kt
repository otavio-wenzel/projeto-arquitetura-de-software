@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package com.example.app_ajudai.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app_ajudai.AppViewModel
import com.example.app_ajudai.categoriasDeFavor

@Composable
fun SearchScreen(
    appViewModel: AppViewModel,
    onNavigateToFavorDetail: (Long) -> Unit
) {
    val focus = LocalFocusManager.current

    val searchInput by appViewModel.searchInput.collectAsStateWithLifecycle()
    val selectedCategories by appViewModel.selectedCategories.collectAsStateWithLifecycle()
    val favores by appViewModel.searchFavores.collectAsStateWithLifecycle()

    var showCategoriesDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pesquisa", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Barra + botões (responsivo com FlowRow)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchInput,
                    onValueChange = { appViewModel.setSearchInput(it) },
                    label = { Text("Buscar por título/descrição") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            appViewModel.applyFilters()
                            focus.clearFocus()
                        }
                    ),
                    // shape simples para evitar imports extras
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                Button(
                    onClick = {
                        appViewModel.applyFilters()
                        focus.clearFocus()
                    },
                    modifier = Modifier.height(56.dp)
                ) { Text("Buscar") }

                OutlinedButton(
                    onClick = { showCategoriesDialog = true },
                    modifier = Modifier.height(56.dp)
                ) {
                    Icon(Icons.Filled.FilterList, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (selectedCategories.isEmpty())
                            "Categorias"
                        else
                            "Categorias (${selectedCategories.size})"
                    )
                }
            }

            // Lista de resultados
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                if (favores.isEmpty()) {
                    item { Text("Nenhum favor encontrado.", style = MaterialTheme.typography.bodyMedium) }
                }
                items(favores, key = { it.id }) { favor ->
                    FavorCard(favor = favor) { onNavigateToFavorDetail(favor.id) }
                }
            }
        }
    }

    // ——————————————————————————————————————————
    // Diálogo de categorias (aplica automaticamente)
    // ——————————————————————————————————————————
    if (showCategoriesDialog) {
        AlertDialog(
            onDismissRequest = { showCategoriesDialog = false },
            title = { Text("Filtrar por categoria") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    categoriasDeFavor.forEach { cat ->
                        val checked = cat in selectedCategories
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(cat, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                            Checkbox(
                                checked = checked,
                                onCheckedChange = { isChecked ->
                                    // alterna no VM
                                    if (isChecked && !checked) appViewModel.toggleCategory(cat)
                                    if (!isChecked && checked)  appViewModel.toggleCategory(cat)
                                    // aplica (NÃO fecha o diálogo)
                                    appViewModel.applyFilters()
                                    // REMOVER esta linha:
                                    // showCategoriesDialog = false
                                }
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    TextButton(
                        onClick = {
                            // limpar tudo (NÃO fecha o diálogo)
                            selectedCategories.forEach { sc -> appViewModel.toggleCategory(sc) }
                            appViewModel.applyFilters()
                            // REMOVER esta linha:
                            // showCategoriesDialog = false
                        }
                    ) { Text("Limpar tudo") }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCategoriesDialog = false }) { Text("Fechar") }
            }
        )
    }
}