package com.example.app_ajudai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.app_ajudai.data.AppDatabase
import com.example.app_ajudai.data.FavorRepositoryRoom
import com.example.app_ajudai.ui.FavorDetailScreen
import com.example.app_ajudai.ui.MainAppScreen
import com.example.app_ajudai.ui.SolicitarFavorScreen
import com.example.app_ajudai.ui.theme.AppajudaiTheme
import androidx.compose.ui.platform.LocalContext
import com.example.app_ajudai.ui.LoginScreen
import android.app.Application
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.collectAsState
import com.example.app_ajudai.ui.*

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
    val context = LocalContext.current

    // AppViewModel
    val appVM: AppViewModel = viewModel(
        factory = AppViewModelFactory(app = context.applicationContext as Application)
    )

    // AuthViewModel
    val authViewModel: AuthViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(context.applicationContext as Application) as T
            }
        }
    )

    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        authViewModel.ensureValidSession()
    }


    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("welcome") {
            WelcomeScreen(
                authViewModel = authViewModel,
                onGoLogin = { navController.navigate("login") },
                onGoSignUp = { navController.navigate("signup") },
                onAutoForwardToMain = {
                    navController.navigate("main") {
                        popUpTo("welcome") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onSuccess = {
                    navController.navigate("main") {
                        popUpTo("welcome") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("signup") {
            SignUpScreen(
                authViewModel = authViewModel,
                onSuccess = {
                    // após criar conta, pode mandar para o login (ou direto pro main, se preferir)
                    navController.navigate("login") {
                        popUpTo("welcome") { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("main") {
            MainAppScreen(
                appViewModel = appVM,
                onNavigateToSolicitarFavor = { navController.navigate("solicitar_favor") },
                onNavigateToFavorDetail = { favorId ->
                    navController.navigate("favor_detail/$favorId")
                },
                authViewModel = authViewModel,
                onRequestLogout = {
                    // derruba sessão e vai para a Welcome limpando a pilha inteira
                    authViewModel.logout()
                    navController.navigate("welcome") {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("solicitar_favor") {
            // coletar o id atual (não deve ser nulo se a guarda de sessão do Main estiver funcionando)
            val userId by authViewModel.currentUserId.collectAsState(initial = null)

            // Se, por qualquer motivo, estiver nulo, você pode redirecionar ou desabilitar a publicação
            if (userId == null) {
                // fallback simples: voltar ou mostrar mensagem
                // navController.popBackStack()
                // ou exibir um texto
                SolicitarFavorScreen(
                    appViewModel = appVM,
                    currentUserId = 0L, // placeholder; o botão ficará habilitado só se você quiser
                    onNavigateBack = { navController.popBackStack() }
                )
            } else {
                SolicitarFavorScreen(
                    appViewModel = appVM,
                    currentUserId = userId!!,                     // 👈 aqui vai o autor
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        composable(
            route = "favor_detail/{favorId}",
            arguments = listOf(navArgument("favorId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("favorId") ?: -1L
            val repo = FavorRepositoryRoom(AppDatabase.get(context).favorDao())
            FavorDetailScreen(
                favorId = id,
                repo = repo,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}