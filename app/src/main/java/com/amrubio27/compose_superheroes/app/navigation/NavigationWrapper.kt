package com.amrubio27.compose_superheroes.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.amrubio27.compose_superheroes.features.featureb.FeatureBScreen
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
                navigateToDetail = {
                    navController.navigate(Detail) {}
                })
        }

        composable<Detail> {
            // Placeholder for Detail screen
            // DetailScreen()
        }

        composable<FeatureB> {
            FeatureBScreen()
        }
    }
}
