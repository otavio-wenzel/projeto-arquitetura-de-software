package com.example.app_ajudai.app.navigation

import android.app.Application
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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.app_ajudai.feature.favor.AppViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import com.example.app_ajudai.feature.auth.AuthViewModel
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_ajudai.feature.inbox.InboxViewModel
import com.example.app_ajudai.core.db.AppDatabase
import com.example.app_ajudai.feature.favor.data.FavorRepositoryRoom
import com.example.app_ajudai.feature.favor.ui.FavorDetailScreen
import com.example.app_ajudai.feature.favor.ui.FeedScreen
import com.example.app_ajudai.feature.inbox.ui.InboxRequestDetailScreen
import com.example.app_ajudai.feature.inbox.ui.InboxScreen
import com.example.app_ajudai.feature.profile.ui.ProfileScreen
import com.example.app_ajudai.feature.favor.ui.SearchScreen

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
    authViewModel: AuthViewModel,
    onRequestLogout: () -> Unit,
    onGoMyPosts: () -> Unit
) {
    val tabsController = rememberNavController()

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
            // FEED -> navega para detalhe DENTRO do tabsController
            composable(Screen.Feed.route) {
                FeedScreen(
                    appViewModel = appViewModel,
                    onAddFavorClick = onNavigateToSolicitarFavor,
                    onFavorClick = { id -> tabsController.navigate("favor_detail/$id") }
                )
            }
            // SEARCH -> idem
            composable(Screen.Search.route) {
                SearchScreen(
                    appViewModel = appViewModel,
                    onNavigateToFavorDetail = { id -> tabsController.navigate("favor_detail/$id") }
                )
            }
            // PROFILE
            composable(Screen.Profile.route) {
                ProfileScreen(
                    authViewModel = authViewModel,
                    onLogout = onRequestLogout,
                    onGoMyPosts = onGoMyPosts,
                    onGoInbox = { tabsController.navigate("inbox") }   // ✅ navega no grafo interno
                )
            }

            // Detalhe do favor
            composable(
                route = "favor_detail/{favorId}",
                arguments = listOf(navArgument("favorId") { type = NavType.LongType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getLong("favorId") ?: -1L
                val context = LocalContext.current
                val repo = remember { FavorRepositoryRoom(AppDatabase.get(context).favorDao()) }
                val currentUserId by authViewModel.currentUserId.collectAsState(initial = null)

                FavorDetailScreen(
                    favorId = id,
                    repo = repo,
                    onNavigateBack = { tabsController.popBackStack() },
                    currentUserId = currentUserId
                )
            }

            // Inbox (lista)
            composable("inbox") {
                val context = LocalContext.current
                val inboxVM: InboxViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return InboxViewModel(context.applicationContext as Application) as T
                    }
                })
                val uid by authViewModel.currentUserId.collectAsState(initial = null)
                if (uid == null) {
                    Text("Sessão expirada.")
                } else {
                    InboxScreen(
                        userId = uid!!,
                        inboxViewModel = inboxVM,
                        onNavigateBack = { tabsController.popBackStack() },
                        onOpenRequest = { reqId -> tabsController.navigate("inbox_detail/$reqId") }
                    )
                }
            }

            // Detalhe da solicitação
            composable(
                route = "inbox_detail/{requestId}",
                arguments = listOf(navArgument("requestId") { type = NavType.LongType })
            ) { backStackEntry ->
                val context = LocalContext.current
                val inboxVM: InboxViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return InboxViewModel(context.applicationContext as Application) as T
                    }
                })
                val reqId = backStackEntry.arguments?.getLong("requestId") ?: -1L
                InboxRequestDetailScreen(
                    requestId = reqId,
                    inboxViewModel = inboxVM,
                    onNavigateBack = { tabsController.popBackStack() }
                )
            }
        }
    }
}