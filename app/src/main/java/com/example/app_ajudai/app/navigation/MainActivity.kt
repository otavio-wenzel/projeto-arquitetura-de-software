package com.example.app_ajudai.app.navigation

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
import com.example.app_ajudai.feature.favor.ui.SolicitarFavorScreen
import com.example.app_ajudai.core.design.theme.AppajudaiTheme
import androidx.compose.ui.platform.LocalContext
import com.example.app_ajudai.feature.auth.ui.LoginScreen
import android.app.Application
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.collectAsState
import com.example.app_ajudai.feature.favor.AppViewModel
import com.example.app_ajudai.app.di.AppViewModelFactory
import com.example.app_ajudai.feature.auth.AuthViewModel
import com.example.app_ajudai.feature.auth.ui.SignUpScreen
import com.example.app_ajudai.feature.auth.ui.WelcomeScreen
import com.example.app_ajudai.feature.favor.ui.ManageMyPostScreen
import com.example.app_ajudai.feature.favor.ui.MyPostsScreen

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
                authViewModel = authViewModel,
                onRequestLogout = {
                    authViewModel.logout()
                    navController.navigate("welcome") {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                onGoMyPosts = {
                    val uid = authViewModel.currentUserId.value ?: return@MainAppScreen
                    navController.navigate("my_posts/$uid")
                }
            )

        }

        // Lista de publicações do usuário
        composable(
            route = "my_posts/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.LongType })
        ) { backStackEntry ->
            val uid = backStackEntry.arguments?.getLong("userId") ?: return@composable
            MyPostsScreen(
                userId = uid,
                appViewModel = appVM,
                onNavigateBack = { navController.popBackStack() },
                onOpenManage = { favorId -> navController.navigate("my_post_manage/$favorId") }
            )
        }

        // Gerenciar (editar/excluir) uma publicação
        composable(
            route = "my_post_manage/{favorId}",
            arguments = listOf(navArgument("favorId") { type = NavType.LongType })
        ) { backStackEntry ->
            val fid = backStackEntry.arguments?.getLong("favorId") ?: return@composable
            ManageMyPostScreen(
                favorId = fid,
                appViewModel = appVM,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Publicar novo favor
        composable("solicitar_favor") {
            val userId by authViewModel.currentUserId.collectAsState(initial = null)
            SolicitarFavorScreen(
                appViewModel = appVM,
                currentUserId = userId ?: 0L,
                onNavigateBack = { navController.popBackStack() }
            )
        }

    }
}