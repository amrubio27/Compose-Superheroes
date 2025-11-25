package com.amrubio27.compose_superheroes.features.list.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amrubio27.compose_superheroes.app.presentation.error.ErrorMapper
import com.amrubio27.compose_superheroes.app.presentation.error.ErrorScreen
import com.amrubio27.compose_superheroes.features.list.presentation.components.superHeroItem.SwipeableSuperheroItem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperheroesListScreen(
    viewModel: SuperHeroesListViewModel = koinViewModel(),
    navigateToDetail: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val errorMapper = remember { ErrorMapper(context) }

    // Efecto para mostrar el Snackbar cuando hay un borrado pendiente
    LaunchedEffect(uiState.pendingDeletion) {
        uiState.pendingDeletion?.let { deletion ->
            val result = snackbarHostState.showSnackbar(
                message = "${deletion.deletedHero.name} eliminado",
                actionLabel = "Deshacer",
                duration = SnackbarDuration.Indefinite // Duracion indefinida para que se quite al hacer le job de borrado
            )

            when (result) {
                SnackbarResult.ActionPerformed -> {
                    // El usuario pulsó "Deshacer"
                    viewModel.undoDelete()
                }

                SnackbarResult.Dismissed -> {
                    // El Snackbar se descartó sin acción (timeout)
                    // El borrado real ya se ejecutó en el ViewModel
                    // podemos reformular el clearPendingDeletion como que elimine los pendientes si otro snackbar aparece
                    // viewModel.clearPendingDeletion()
                }
            }
        }
    }

    // Effect to show Snackbar for errors when data exists
    LaunchedEffect(uiState.error) {
        if (uiState.error != null && uiState.superHeroes.isNotEmpty()) {
            val errorModel = errorMapper.map(uiState.error!!)
            val result = snackbarHostState.showSnackbar(
                message = errorModel.description, // Or title, depending on what's more appropriate
                actionLabel = context.getString(com.amrubio27.compose_superheroes.R.string.retry),
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.refreshSuperHeroes()
            }
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Eliminamos el Spacer superior para que el stickyHeader funcione bien desde arriba

            if (uiState.error != null && uiState.superHeroes.isEmpty()) {
                ErrorScreen(
                    errorUiModel = errorMapper.map(uiState.error!!),
                    onRetry = { viewModel.fetchSuperHeroes() }
                )
            } else {
                PullToRefreshBox(
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = { viewModel.refreshSuperHeroes() },
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        stickyHeader {
                            // Contenedor para el buscador con fondo para tapar el contenido al hacer scroll
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.background)
                                    .padding(bottom = 16.dp, top = 8.dp), // Reduced top padding
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                OutlinedTextField(
                                    value = uiState.searchQuery,
                                    onValueChange = { viewModel.onSearchQueryChange(it) },
                                    label = { Text("Search superhero") },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        if (uiState.isLoading) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "Loading heroes...",
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    CircularProgressIndicator()
                                }
                            }
                        } else if (uiState.superHeroes.isEmpty() && uiState.searchQuery.isNotEmpty()) {
                            item {
                                Text(
                                    text = "No superheroes found with \"${uiState.searchQuery}\"",
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        } else {
                            itemsIndexed(
                                items = uiState.superHeroes,
                                key = { _, item -> item.id }
                            ) { _, item ->
                                SwipeableSuperheroItem(
                                    hero = item,
                                    onDismiss = { heroId ->
                                        viewModel.deleteHeroOptimistic(heroId)
                                    },
                                    navigateToDetail = navigateToDetail
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
