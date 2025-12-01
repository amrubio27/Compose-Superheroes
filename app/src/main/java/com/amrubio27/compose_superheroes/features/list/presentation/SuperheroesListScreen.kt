package com.amrubio27.compose_superheroes.features.list.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amrubio27.compose_superheroes.R
import com.amrubio27.compose_superheroes.app.presentation.error.ErrorMapper
import com.amrubio27.compose_superheroes.app.presentation.error.ErrorScreen
import com.amrubio27.compose_superheroes.features.list.presentation.components.SuperheroesSearchBar
import com.amrubio27.compose_superheroes.features.list.presentation.components.superHeroItem.SwipeableSuperheroItem
import com.amrubio27.compose_superheroes.ui.theme.dimens
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

    LaunchedEffect(uiState.pendingDeletion) {
        uiState.pendingDeletion?.let { deletion ->
            when (
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.hero_deleted, deletion.deletedHero.name),
                    actionLabel = context.getString(R.string.undo),
                    duration = SnackbarDuration.Indefinite
                )
            ) {
                SnackbarResult.ActionPerformed -> viewModel.undoDelete()
                SnackbarResult.Dismissed -> Unit
            }
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.takeIf { uiState.superHeroes.isNotEmpty() }?.let { error ->
            val result = snackbarHostState.showSnackbar(
                message = errorMapper.map(error).description,
                actionLabel = context.getString(R.string.retry),
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.refreshSuperHeroes()
            }
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            SuperheroesSearchBar(
                query = uiState.searchQuery,
                onQueryChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.paddingMedium)
            )
        }, snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = MaterialTheme.dimens.paddingMedium),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            stringResource(R.string.loading_heroes),
                            modifier = Modifier.padding(bottom = MaterialTheme.dimens.paddingSmall)
                        )
                        CircularProgressIndicator()
                    }
                }

                uiState.error != null && uiState.superHeroes.isEmpty() -> {
                    ErrorScreen(
                        errorUiModel = errorMapper.map(uiState.error!!),
                        onRetry = { viewModel.fetchSuperHeroes() },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                uiState.superHeroes.isEmpty() && uiState.searchQuery.isNotEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(
                                R.string.no_superheroes_found,
                                uiState.searchQuery
                            ),
                            modifier = Modifier.padding(MaterialTheme.dimens.paddingMedium),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                else -> {
                    PullToRefreshBox(
                        isRefreshing = uiState.isRefreshing,
                        onRefresh = { viewModel.refreshSuperHeroes() },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
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
