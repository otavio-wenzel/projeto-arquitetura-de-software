package com.example.app_ajudai

/**
 * Este ficheiro guarda constantes (valores fixos) que são
 * usados em várias partes da aplicação.
 */

// --- CATEGORIAS ---
val categoriasDeFavor = listOf(
    "Pequenos serviços domésticos",
    "Ir ao mercado/farmácia",
    "Consertos simples",
    "Ajudar com crianças/idosos",
    "Cuidar de plantas/animais",
    "Dar carona ou buscar algo",
    "Itens Perdidos e Encontrados",
    "Espaço Pet",
    "Dica Local"
)

// --- DADOS FICTÍCIOS (MOVIDOS DO FEEDSCREEN) ---

// O "molde" do que é um favor
data class Favor(
    val id: String,
    val titulo: String,
    val descricao: String,
    val categoria: String
    // No futuro, podemos adicionar: val userId: String (quem pediu)
)

// A lista de favores fictícios
val mockFavores = listOf(
    Favor(
        id = "1",
        titulo = "Preciso de ajuda com compras",
        descricao = "Alguém pode ir ao mercado para mim? Estou doente e não posso sair de casa. Seriam apenas alguns itens essenciais.",
        categoria = "Ir ao mercado/farmácia"
    ),
    Favor(
        id = "2",
        titulo = "Consertar torneira",
        descricao = "Minha torneira da cozinha está a pingar sem parar, alguém que perceba de canalização pode dar uma olhada? Eu tenho as ferramentas.",
        categoria = "Consertos simples"
    ),
    Favor(
        id = "3",
        titulo = "Cuidar de Gato",
        descricao = "Vou viajar no fim de semana (Sábado e Domingo) e preciso que alguém vá a minha casa uma vez por dia para alimentar o meu gato, o Félix.",
        categoria = "Cuidar de plantas/animais"
    )
)