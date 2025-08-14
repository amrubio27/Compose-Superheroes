package com.amrubio27.compose_superheroes.app.navigation

import androidx.compose.ui.graphics.vector.ImageVector

//Segun la documentacion de Navigation Component
data class NavItem<T : Any>(val name: String, val route: T, val icon: ImageVector)
