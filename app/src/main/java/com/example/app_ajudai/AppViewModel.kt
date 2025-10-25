package com.example.app_ajudai

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * O "cérebro" (ViewModel) compartilhado pela nossa aplicação.
 * Ele guarda o estado dos filtros de pesquisa.
 */
class AppViewModel : ViewModel() {

    // --- FILTROS DE CATEGORIA ---

    // 1. O Estado "privado" e "mutável" (só o ViewModel pode alterar)
    // Guarda um "Set" (conjunto) de strings, que são as categorias selecionadas.
    private val _selectedCategories = MutableStateFlow(emptySet<String>())

    // 2. O Estado "público" e "somente leitura" (as telas podem ler)
    // As telas vão "ouvir" este StateFlow para saber quando os filtros mudam.
    val selectedCategories = _selectedCategories.asStateFlow()

    /**
     * Ação que as telas podem chamar para adicionar ou remover um filtro de categoria.
     */
    fun toggleCategory(category: String) {
        // 'update' é uma forma segura de alterar o estado
        _selectedCategories.update { currentSet ->
            if (currentSet.contains(category)) {
                // Se já continha, cria um novo Set SEM ela
                currentSet - category
            } else {
                // Se não continha, cria um novo Set COM ela
                currentSet + category
            }
        }
    }
}