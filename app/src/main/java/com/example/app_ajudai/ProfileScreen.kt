package com.example.app_ajudai

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app_ajudai.ui.theme.AppajudaiTheme


// --- 1. Molde de Dados para o Perfil (baseado no RF02) ---
data class UserProfile(
    val nome: String,
    val fotoResourceId: Int,
    val localizacao: String,
    val nivelConfianca: Float,
    val temSeloAjuda: Boolean
)

// --- 2. Dados Fictícios (Mock Data) ---
val mockProfile = UserProfile(
    nome = "Dona Clotilde",
    fotoResourceId = R.drawable.ajudai_transparente,
    localizacao = "Copacabana, RJ",
    nivelConfianca = 4.5f,
    temSeloAjuda = true
)

// --- 3. A Tela de Perfil (RF02) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    val profile = mockProfile

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Meu Perfil",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
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
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = profile.fotoResourceId),
                contentDescription = "Foto de Perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = profile.nome,
                    style = MaterialTheme.typography.titleLarge
                )

                if (profile.temSeloAjuda) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        // --- O CAMINHO CORRETO ---
                        imageVector = Icons.Filled.Verified,
                        contentDescription = "Selo de Ajuda",
                        tint = MaterialTheme.colorScheme.tertiary, // Cor Oliva
                        modifier = Modifier.size(24.dp)
                    )
                }
            }


            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(
                // --- O CAMINHO CORRETO ---
                icon = Icons.Filled.LocationOn,
                text = profile.localizacao
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(
                // --- O CAMINHO CORRETO ---
                icon = Icons.Filled.Star,
                text = "Confiança: ${profile.nivelConfianca} / 5.0"
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* TODO: Implementar logout */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Sair", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    AppajudaiTheme {
        ProfileScreen()
    }
}