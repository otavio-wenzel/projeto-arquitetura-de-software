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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppajudaiTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
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
        factory = AppViewModelFactory(app = context.applicationContext as android.app.Application)
    )
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("main") {
            MainAppScreen(
                appViewModel = appVM,
                onNavigateToSolicitarFavor = { navController.navigate("solicitar_favor") },
                onNavigateToFavorDetail = { favorId -> navController.navigate("favor_detail/$favorId") }
            )
        }
        composable("solicitar_favor") {
            SolicitarFavorScreen(
                appViewModel = appVM,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(
            "favor_detail/{favorId}",
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