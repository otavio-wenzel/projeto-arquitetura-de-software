package com.example.app_ajudai.data

import kotlinx.coroutines.flow.Flow

interface FavorRepository {
    fun observarTodos(): Flow<List<Favor>>
    fun observarPorId(id: Long): Flow<Favor?>
    fun observarFiltrados(query: String?, categorias: Set<String>): Flow<List<Favor>>
    suspend fun inserir(titulo: String, descricao: String, categoria: String)
    suspend fun atualizar(favor: Favor)
    suspend fun deletar(favor: Favor)
}

// data/FavorRepository.kt
class FavorRepositoryRoom(private val dao: FavorDao) : FavorRepository {

    override fun observarTodos() = dao.observarTodos()

    override fun observarPorId(id: Long) = dao.observarPorId(id)

    override fun observarFiltrados(query: String?, categorias: Set<String>) =
        if (categorias.isEmpty())
            dao.observarFiltradosSemCategoria(query)
        else
            dao.observarFiltradosComCategorias(query, categorias.toList())

    override suspend fun inserir(titulo: String, descricao: String, categoria: String) {
        dao.inserir(Favor(titulo = titulo, descricao = descricao, categoria = categoria))
    }

    override suspend fun atualizar(favor: Favor) = dao.atualizar(favor)

    override suspend fun deletar(favor: Favor) = dao.deletar(favor)
}