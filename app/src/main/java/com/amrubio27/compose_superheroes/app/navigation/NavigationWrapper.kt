package com.amrubio27.compose_superheroes.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.amrubio27.compose_superheroes.features.detail.presentation.SuperHeroesDetailScreen
import com.amrubio27.compose_superheroes.features.featureb.presentation.FeatureBScreen
import com.amrubio27.compose_superheroes.features.list.presentation.SuperheroesListScreen

@Composable
fun NavigationWrapper(
    navController: NavHostController, modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier
    ) {
        composable<Home> {
            SuperheroesListScreen(
                navigateToDetail = { detailId ->
                    navController.navigate(Detail(id = detailId)) {}
                }
            )
        }

        composable<Detail> { navBackStackEntry ->

        SuperHeroesDetailScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<FeatureB> {
            FeatureBScreen()
        }
    }
}
