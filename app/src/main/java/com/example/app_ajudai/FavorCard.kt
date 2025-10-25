package com.example.app_ajudai

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Este é o Composable reutilizável para mostrar um Card de Favor.
 * Ele é usado tanto no FeedScreen quanto no SearchScreen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavorCard(
    favor: Favor,
    modifier: Modifier = Modifier,
    onClick: () -> Unit // Recebe a função de clique
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = favor.titulo,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = favor.categoria,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )
            Text(
                text = favor.descricao,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}