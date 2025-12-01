package com.amrubio27.compose_superheroes.features.detail.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amrubio27.compose_superheroes.R
import com.amrubio27.compose_superheroes.features.detail.presentation.components.AppearanceCard
import com.amrubio27.compose_superheroes.features.detail.presentation.components.BiographyCard
import com.amrubio27.compose_superheroes.features.detail.presentation.components.ConnectionsCard
import com.amrubio27.compose_superheroes.features.detail.presentation.components.DetailTopAppBar
import com.amrubio27.compose_superheroes.features.detail.presentation.components.ErrorState
import com.amrubio27.compose_superheroes.features.detail.presentation.components.HeroImageHeader
import com.amrubio27.compose_superheroes.features.detail.presentation.components.RadarChartContainer
import com.amrubio27.compose_superheroes.features.detail.presentation.components.SectionTitle
import com.amrubio27.compose_superheroes.features.detail.presentation.components.WorkCard
import com.amrubio27.compose_superheroes.ui.theme.dimens
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperHeroesDetailScreen(
    viewModel: SuperHeroesDetailViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            DetailTopAppBar(
                onNavigateBack = onNavigateBack,
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding()) // Ignore top padding for immersive image
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.superHero != null -> {
                    DetailContent(
                        hero = uiState.superHero!!
                    )
                }

                else -> {
                    ErrorState()
                }
            }
        }
    }
}

@Composable
fun DetailContent(
    hero: SuperHeroDetailUiModel
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.paddingMedium),
    ) {
        item {
            HeroImageHeader(hero = hero)
        }

        item {
            SectionTitle(title = stringResource(R.string.detail_section_power_stats))
            RadarChartContainer(stats = hero.powerStats)
        }

        item {
            SectionTitle(title = stringResource(R.string.detail_section_biography))
            BiographyCard(bio = hero.biography)
        }

        item {
            SectionTitle(title = stringResource(R.string.detail_section_appearance))
            AppearanceCard(appearance = hero.appearance)
        }

        item {
            SectionTitle(title = stringResource(R.string.detail_section_work))
            WorkCard(work = hero.work)
        }

        item {
            SectionTitle(title = stringResource(R.string.detail_section_connections))
            ConnectionsCard(connections = hero.connections)
        }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.paddingExtraLarge))
        }
    }
}
