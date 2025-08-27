package com.amrubio27.compose_superheroes.features.list.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amrubio27.compose_superheroes.features.list.presentation.components.superHeroItem.SuperheroItem
import org.koin.androidx.compose.koinViewModel

private object ScreenConstants {
    const val ANIMATION_DURATION_MS = 300
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperheroesListScreen(
    viewModel: SuperHeroesListViewModel = koinViewModel(), navigateToDetail: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.fetchSuperHeroes()
    }

    LaunchedEffect(uiState.pendingDeletion) {
        uiState.pendingDeletion?.let { pendingDeletion ->
            val result = snackbarHostState.showSnackbar(
                message = "Superhéroe ${pendingDeletion.heroName} eliminado",
                actionLabel = "Deshacer",
                duration = SnackbarDuration.Long
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.undoDelete()
            } else {
                viewModel.clearMessages()
            }
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(message = error)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Buscar superhéroes") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                Text("Cargando superhéroes...", modifier = Modifier.padding(16.dp))
                CircularProgressIndicator()
            } else {
                LazyColumn {
                    itemsIndexed(
                        items = uiState.superHeroes, key = { _, hero -> hero.id }) { _, hero ->
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { dismissValue ->
                                val shouldDelete =
                                    (dismissValue != SwipeToDismissBoxValue.Settled) && (dismissValue == SwipeToDismissBoxValue.StartToEnd || dismissValue == SwipeToDismissBoxValue.EndToStart)
                                if (shouldDelete) {
                                    viewModel.deleteHero(hero.id, hero.name)
                                    true
                                } else {
                                    false
                                }
                            })

                        SwipeToDismissBox(
                            state = dismissState,
                            backgroundContent = { DismissBackground(dismissState) },
                            content = {
                                SuperheroItem(
                                    hero = hero, navigateToDetail = navigateToDetail
                                )
                            })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DismissBackground(
    dismissState: androidx.compose.material3.SwipeToDismissBoxState
) {
    val isBeingDismissed = dismissState.targetValue != SwipeToDismissBoxValue.Settled

    val backgroundColor by animateColorAsState(
        targetValue = if (isBeingDismissed) Color.Red else MaterialTheme.colorScheme.surface,
        animationSpec = tween(ScreenConstants.ANIMATION_DURATION_MS),
        label = "dismiss_background_color"
    )

    val iconScale by animateFloatAsState(
        targetValue = if (isBeingDismissed) 1f else 0.75f,
        animationSpec = tween(ScreenConstants.ANIMATION_DURATION_MS),
        label = "delete_icon_scale"
    )

    val contentAlignment = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
        else -> Alignment.Center
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 20.dp),
        contentAlignment = contentAlignment
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Eliminar superhéroe",
            modifier = Modifier.scale(iconScale),
            tint = Color.White
        )
    }
}
