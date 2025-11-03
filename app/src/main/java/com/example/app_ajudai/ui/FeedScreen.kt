package com.example.app_ajudai.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app_ajudai.AppViewModel
import com.example.app_ajudai.R
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    appViewModel: AppViewModel,
    onAddFavorClick: () -> Unit,
    onFavorClick: (Long) -> Unit
) {
    val favores by appViewModel.feedFavores.collectAsStateWithLifecycle()

    // controle de posição da lista
    val listState = rememberLazyListState()
    val lastCount = remember { mutableStateOf(0) }

    // garante topo no primeiro draw
    LaunchedEffect(Unit) { listState.scrollToItem(0) }

    // ao entrar item novo, rola para o topo
    LaunchedEffect(favores.size) {
        if (favores.isNotEmpty() && (lastCount.value == 0 || favores.size > lastCount.value)) {
            listState.animateScrollToItem(0)
        }
        lastCount.value = favores.size
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.ajudai_transparente),
                        contentDescription = "Logo Ajudaí",
                        modifier = Modifier.height(36.dp)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.shadow(elevation = 4.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddFavorClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) { Icon(Icons.Filled.Add, contentDescription = "Solicitar Favor") }
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
        ) {
            items(favores, key = { it.id }) { favor ->
                FavorCard(favor = favor) { onFavorClick(favor.id) }
            }
        }
    }
}