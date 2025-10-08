package com.amrubio27.compose_superheroes.features.list.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amrubio27.compose_superheroes.features.list.presentation.components.superHeroItem.SuperheroItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun SuperheroesListScreen(
    viewModel: SuperHeroesListViewModel = koinViewModel(),
    navigateToDetail: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchSuperHeroes()
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                label = { Text("Search superhero") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                Text("Loading heroes...", modifier = Modifier.padding(16.dp))
                CircularProgressIndicator()
            } else if (uiState.superHeroes.isEmpty() && uiState.searchQuery.isNotEmpty()) {
                Text(
                    text = "No superheroes found with \"${uiState.searchQuery}\"",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn {
                    itemsIndexed(uiState.superHeroes, key = { _, item -> item.id }) { index, item ->
                        Row {
                            SuperheroItem(hero = item, navigateToDetail = navigateToDetail)
                        }
                    }
                }
            }
        }
    }
}
