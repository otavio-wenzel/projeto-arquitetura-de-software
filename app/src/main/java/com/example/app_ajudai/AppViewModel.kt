package com.example.app_ajudai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_ajudai.data.Favor
import com.example.app_ajudai.data.FavorRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(private val repo: FavorRepository) : ViewModel() {

    // -----------------------------
    // ESTADO DE BUSCA
    // -----------------------------

    // Texto que o usuário DIGITA (campo de entrada)
    private val _searchInput = MutableStateFlow("")
    val searchInput: StateFlow<String> = _searchInput.asStateFlow()

    // Texto que está APLICADO no filtro (apertou Buscar/Enter)
    private val _appliedQuery = MutableStateFlow<String?>(null)

    // Categorias selecionadas
    private val _selectedCategories = MutableStateFlow(emptySet<String>())
    val selectedCategories: StateFlow<Set<String>> = _selectedCategories.asStateFlow()

    fun setSearchInput(q: String) { _searchInput.value = q }

    fun toggleCategory(category: String) {
        _selectedCategories.update { set ->
            if (set.contains(category)) set - category else set + category
        }
        // aplica imediatamente ao tocar no chip
        applyFilters()
    }

    fun clearFilters() {
        _searchInput.value = ""
        _selectedCategories.value = emptySet()
        _appliedQuery.value = null
    }

    // Aplica o texto digitado como query de fato
    fun applyFilters() {
        val q = _searchInput.value.trim()
        _appliedQuery.value = if (q.isBlank()) null else q
    }

    /**
     * Resultado da busca (com base em appliedQuery + categorias).
     * Repo decide entre SQL "com" ou "sem" categorias.
     */
    val searchFavores: StateFlow<List<Favor>> =
        combine(_appliedQuery, _selectedCategories) { q, cats -> q to cats }
            .flatMapLatest { (q, cats) -> repo.observarFiltrados(q, cats) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // -----------------------------
    // FEED (sem filtros)
    // -----------------------------
    val feedFavores: StateFlow<List<Favor>> =
        repo.observarTodos()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // -----------------------------
    // AÇÕES CRUD
    // -----------------------------
    fun criarFavor(userId: Long, titulo: String, descricao: String, categoria: String) =
        viewModelScope.launch {
            repo.inserir(userId, titulo, descricao, categoria)
        }

    fun deletarFavor(favor: Favor) = viewModelScope.launch { repo.deletar(favor) }

    // 🔹 fluxo dos favores do usuário logado
    fun observarMeusFavores(userId: Long) = repo.observarDoUsuario(userId)

    // 🔹 atualizar um favor existente
    fun atualizarFavor(editado: Favor) = viewModelScope.launch { repo.atualizar(editado) }

    fun observarPorId(id: Long) = repo.observarPorId(id)

}