package com.amrubio27.compose_superheroes.features.detail.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun SuperHeroesDetailScreen(
    viewModel: SuperHeroesDetailViewModel = koinViewModel(),
    id: Int,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = id) {
        viewModel.fetchSuperHeroById(id)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        topBar = {
            DetailTopAppBar(
                onNavigateBack = onNavigateBack
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .fillMaxSize()
        ) {

            if (uiState.isLoading) {
                Spacer(Modifier.weight(1f))
                Text("Cargando héroe...", modifier = Modifier.padding(16.dp))
                CircularProgressIndicator()
                Spacer(Modifier.weight(1f))
            } else {
                Text(
                    uiState.superHero?.name ?: "Héroe no encontrado",
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    uiState.superHero?.slug ?: "Slug no disponible",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopAppBar(
    modifier: Modifier = Modifier, onNavigateBack: () -> Unit
) {
    TopAppBar(
        title = { Text("Detail") },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .clickable(
                        onClick = onNavigateBack
                    ),
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    )
}

