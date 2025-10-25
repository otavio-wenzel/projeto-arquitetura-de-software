package com.example.app_ajudai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
// Importa o ícone "AutoMirrored" (corrigido)
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app_ajudai.ui.theme.AppajudaiTheme

/**
 * Tela para o RF03: Solicitar Favor
 * @param onNavigateBack Ação para voltar à tela anterior
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitarFavorScreen(
    onNavigateBack: () -> Unit
) {
    // Estados
    var selectedCategory by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) } // Estado para controlar o dropdown
    var descricao by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pedir um Favor", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    // Botão para voltar atrás
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            // Usa o novo ícone AutoMirrored (corrigido)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Descreva o favor que precisa",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // CAMPO DE CATEGORIA (DROPDOWN)
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = !isExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                // O campo de texto que o utilizador vê
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {}, // Não permitimos escrita direta
                    readOnly = true, // Apenas leitura
                    label = { Text("Selecione a Categoria") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor() // Liga este texto ao menu
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // O Menu que abre
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false } // Fecha se clicar fora
                ) {
                    // Cria um item de menu para cada categoria da lista (de AppConstants.kt)
                    categoriasDeFavor.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria, style = MaterialTheme.typography.bodyLarge) },
                            onClick = {
                                selectedCategory = categoria // Atualiza o texto
                                isExpanded = false // Fecha o menu
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Descrição
            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição detalhada do favor...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.weight(1f)) // Empurra o botão para baixo

            // Botão Publicar
            Button(
                onClick = {
                    if (selectedCategory.isNotEmpty() && descricao.isNotEmpty()) {
                        onNavigateBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = selectedCategory.isNotEmpty() && descricao.isNotEmpty()
            ) {
                Text("Publicar Pedido", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SolicitarFavorScreenPreview() {
    AppajudaiTheme {
        SolicitarFavorScreen(onNavigateBack = {})
    }
}