package com.example.app_ajudai.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// data/FavorDao.kt
@Dao
interface FavorDao {

    @Insert
    suspend fun inserir(favor: Favor): Long

    @Update
    suspend fun atualizar(favor: Favor)

    @Delete
    suspend fun deletar(favor: Favor)

    @Query("SELECT * FROM favor ORDER BY createdAt DESC")
    fun observarTodos(): Flow<List<Favor>>

    @Query("SELECT * FROM favor WHERE id = :id LIMIT 1")
    fun observarPorId(id: Long): Flow<Favor?>

    // QUANDO HÁ categorias selecionadas
    @Query("""
        SELECT * FROM favor
        WHERE (:query IS NULL OR :query = '' 
           OR titulo LIKE '%' || :query || '%' 
           OR descricao LIKE '%' || :query || '%')
          AND categoria IN (:categorias)
        ORDER BY createdAt DESC
    """)
    fun observarFiltradosComCategorias(
        query: String?,
        categorias: List<String>
    ): Flow<List<Favor>>

    // QUANDO NÃO HÁ categorias selecionadas
    @Query("""
        SELECT * FROM favor
        WHERE (:query IS NULL OR :query = '' 
           OR titulo LIKE '%' || :query || '%' 
           OR descricao LIKE '%' || :query || '%')
        ORDER BY createdAt DESC
    """)
    fun observarFiltradosSemCategoria(
        query: String?
    ): Flow<List<Favor>>
}