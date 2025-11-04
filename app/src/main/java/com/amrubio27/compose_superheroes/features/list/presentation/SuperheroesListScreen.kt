package com.amrubio27.compose_superheroes.features.list.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amrubio27.compose_superheroes.features.list.presentation.components.superHeroItem.SwipeableSuperheroItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun SuperheroesListScreen(
    viewModel: SuperHeroesListViewModel = koinViewModel(),
    navigateToDetail: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.fetchSuperHeroes()
    }

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
                    //podemos reformular el clearPendingDeletion como que elimine los pendientes si otro snackbar aparece
                    //viewModel.clearPendingDeletion()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Search") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                Text("Cargando héroes...", modifier = Modifier.padding(16.dp))
                CircularProgressIndicator()
            } else {
                LazyColumn {
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
