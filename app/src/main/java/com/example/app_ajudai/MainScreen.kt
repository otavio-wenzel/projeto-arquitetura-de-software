package com.example.app_ajudai

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons // <<< A ÚNICA IMPORTAÇÃO DE ÍCONE
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.app_ajudai.ui.theme.AppajudaiTheme
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Person


// (Screen e items continuam iguais)
sealed class Screen(val route: String, val label: String, val icon: @Composable () -> Unit) {
    object Feed : Screen("feed", "Início", { Icon(Icons.Filled.Home, contentDescription = "Início") })
    object Search : Screen("search", "Pesquisa", { Icon(Icons.Filled.Search, contentDescription = "Pesquisa") })
    object Profile : Screen("profile", "Perfil", { Icon(Icons.Filled.Person, contentDescription = "Perfil") })
}
val items = listOf(Screen.Feed, Screen.Search, Screen.Profile)


@Composable
fun MainAppScreen(
    appViewModel: AppViewModel,
    onNavigateToSolicitarFavor: () -> Unit,
    onNavigateToFavorDetail: (String) -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.shadow(elevation = 8.dp)
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { screen.icon() },
                        label = { Text(screen.label, style = MaterialTheme.typography.bodyMedium) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Feed.route,
            Modifier.padding(innerPadding)
        ) {
            // FeedScreen (agora simples)
            composable(Screen.Feed.route) {
                FeedScreen(
                    onAddFavorClick = onNavigateToSolicitarFavor,
                    onFavorClick = onNavigateToFavorDetail
                )
            }
            // SearchScreen (agora inteligente)
            composable(Screen.Search.route) {
                SearchScreen(
                    appViewModel = appViewModel,
                    // Passa a navegação para o SearchScreen também
                    onNavigateToFavorDetail = onNavigateToFavorDetail
                )
            }
            composable(Screen.Profile.route) { ProfileScreen() }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainAppScreenPreview() {
    val fakeViewModel = AppViewModel()
    AppajudaiTheme {
        MainAppScreen(
            appViewModel = fakeViewModel,
            onNavigateToSolicitarFavor = {},
            onNavigateToFavorDetail = {}
        )
    }
}