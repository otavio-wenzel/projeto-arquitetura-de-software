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

    val appVM: AppViewModel = viewModel(
        factory = AppViewModelFactory(app = context.applicationContext as Application)
    )

    val authViewModel: AuthViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(context.applicationContext as Application) as T
            }
        }
    )

    // NÃO use mais currentUserId pra decidir startDestination aqui
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "welcome"   // ⬅️ sempre começa na Welcome
    ) {
        composable("welcome") {
            WelcomeScreen(
                authViewModel = authViewModel,            // ⬅️ passamos o VM
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
                authViewModel = authViewModel
            )
        }

        composable("solicitar_favor") {
            SolicitarFavorScreen(
                appViewModel = appVM,
                onNavigateBack = { navController.popBackStack() }
            )
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