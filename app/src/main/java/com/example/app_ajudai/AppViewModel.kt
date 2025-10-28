package com.example.app_ajudai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_ajudai.data.Favor
import com.example.app_ajudai.data.FavorRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(private val repo: FavorRepository) : ViewModel() {

    // Filtros
    private val _selectedCategories = MutableStateFlow(emptySet<String>())
    val selectedCategories: StateFlow<Set<String>> = _selectedCategories.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun toggleCategory(category: String) {
        _selectedCategories.update { set -> if (set.contains(category)) set - category else set + category }
    }

    fun setSearchQuery(q: String) { _searchQuery.value = q }

    // Lista observável (filtrada)
    val favores: StateFlow<List<Favor>> =
        combine(_searchQuery, _selectedCategories) { q, cats -> q to cats }
            .flatMapLatest { (q, cats) -> repo.observarFiltrados(q.ifBlank { null }, cats) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Ações CRUD
    fun criarFavor(titulo: String, descricao: String, categoria: String) = viewModelScope.launch {
        repo.inserir(titulo, descricao, categoria)
    }

    fun deletarFavor(favor: Favor) = viewModelScope.launch { repo.deletar(favor) }
}