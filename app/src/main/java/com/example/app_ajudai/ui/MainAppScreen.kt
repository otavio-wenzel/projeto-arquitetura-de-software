package com.example.app_ajudai.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.app_ajudai.AppViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import com.example.app_ajudai.AuthViewModel
import androidx.compose.runtime.*

sealed class Screen(val route: String, val label: String, val icon: @Composable () -> Unit) {
    object Feed : Screen("feed", "Início", { Icon(Icons.Filled.Home, contentDescription = "Início") })
    object Search : Screen("search", "Pesquisa", { Icon(Icons.Filled.Search, contentDescription = "Pesquisa") })
    object Profile : Screen("profile", "Perfil", { Icon(Icons.Filled.Person, contentDescription = "Perfil") })
}
private val items = listOf(Screen.Feed, Screen.Search, Screen.Profile)

@Composable
fun MainAppScreen(
    appViewModel: AppViewModel,
    onNavigateToSolicitarFavor: () -> Unit,
    onNavigateToFavorDetail: (Long) -> Unit,
    authViewModel: AuthViewModel,
    onRequestLogout: () -> Unit,
    onGoMyPosts: () -> Unit
) {
    val tabsController = rememberNavController()

    // 🚧 Guarda de sessão dentro do "main":
    // Se a sessão cair (logout), pedimos para o Nav raiz ir para a Welcome.
    val currentUserId by authViewModel.currentUserId.collectAsState(initial = null)
    LaunchedEffect(currentUserId) {
        if (currentUserId == null) onRequestLogout()
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.shadow(elevation = 8.dp)
            ) {
                val navBackStackEntry by tabsController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { screen.icon() },
                        label = { Text(screen.label, style = MaterialTheme.typography.bodyMedium) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            tabsController.navigate(screen.route) {
                                popUpTo(tabsController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = tabsController,
            startDestination = Screen.Feed.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Feed.route) {
                FeedScreen(
                    appViewModel = appViewModel,
                    onAddFavorClick = onNavigateToSolicitarFavor,
                    onFavorClick = onNavigateToFavorDetail
                )
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    appViewModel = appViewModel,
                    onNavigateToFavorDetail = onNavigateToFavorDetail
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    authViewModel = authViewModel,
                    onLogout = onRequestLogout,   // 🔗 repassa ação de sair
                    onGoMyPosts = onGoMyPosts
                )
            }
        }
    }
}