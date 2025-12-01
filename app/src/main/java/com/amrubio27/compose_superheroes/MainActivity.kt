package com.amrubio27.compose_superheroes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.amrubio27.compose_superheroes.app.navigation.FeatureB
import com.amrubio27.compose_superheroes.app.navigation.Home
import com.amrubio27.compose_superheroes.app.navigation.MyNavigationBarAdvanced
import com.amrubio27.compose_superheroes.app.navigation.NavigationWrapper
import com.amrubio27.compose_superheroes.ui.theme.ComposeSuperheroesTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeSuperheroesTheme {
                val navController = rememberNavController()

                val currentRoute =
                    navController.currentBackStackEntryAsState().value?.destination?.route
                val showBottomNav = currentRoute in listOf(
                    Home::class.qualifiedName,
                    FeatureB::class.qualifiedName
                )

                Scaffold(
                    bottomBar = {
                        if (showBottomNav) {
                            MyNavigationBarAdvanced(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        NavigationWrapper(navController = navController)
                    }
                }
            }
        }
    }
}
