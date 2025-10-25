package com.example.app_ajudai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
// --- IMPORT ADICIONADO ---
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.app_ajudai.ui.theme.AppajudaiTheme
import com.example.app_ajudai.MainAppScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppajudaiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}


@Composable
fun AppNavigation() {
    // --- O VIEWMODEL É CRIADO AQUI ---
    // Isto cria o ViewModel e o liga ao ciclo de vida da navegação
    val appViewModel: AppViewModel = viewModel()

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable(route = "login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable(route = "main") {
            MainAppScreen(
                // --- PASSAMOS O VIEWMODEL PARA A TELA PRINCIPAL ---
                appViewModel = appViewModel,

                onNavigateToSolicitarFavor = {
                    navController.navigate("solicitar_favor")
                },
                onNavigateToFavorDetail = { favorId ->
                    navController.navigate("favor_detail/$favorId")
                }
            )
        }

        composable(route = "solicitar_favor") {
            SolicitarFavorScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "favor_detail/{favorId}",
            arguments = listOf(navArgument("favorId") { type = NavType.StringType })
        ) { backStackEntry ->
            FavorDetailScreen(
                favorId = backStackEntry.arguments?.getString("favorId"),
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}