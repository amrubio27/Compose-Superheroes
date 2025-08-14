package com.amrubio27.compose_superheroes.app.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlin.reflect.KClass

/**
 * Función de extensión que verifica si un destino de navegación tiene una ruta específica.
 * Compara el nombre simple de la clase con la ruta del destino.
 */
fun NavDestination.hasRoute(route: KClass<*>): Boolean {
    return route.simpleName == this.route
}

@Composable
fun MyNavigationBarAdvanced(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val itemList = listOf(
        NavItem(
            name = "SuperHeroList",
            icon = Icons.AutoMirrored.Filled.List,
            route = Home
        ),
        NavItem(
            name = "Feature-B",
            icon = Icons.Default.Person,
            route = FeatureB
        )
    )

    // Obtener la ruta actual de navegación
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    ) {
        itemList.forEach { navItem ->
            // Usar hierarchy para verificar la ruta y manejar navegación anidada
            val isSelected = currentDestination?.hierarchy?.any {
                it.hasRoute(navItem.route::class)
            } ?: false

            MyOwnItem(
                navItem = navItem,
                isSelected = isSelected
            ) {
                // Solo navegar si no estamos ya en esa ruta
                val isCurrentDestination = currentDestination?.hierarchy?.any {
                    it.hasRoute(navItem.route::class)
                } ?: false

                if (!isCurrentDestination) {
                    navController.navigate(navItem.route) {
                        // Evitar múltiples instancias de la misma pantalla en el back stack
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Restaurar estado al regresar a esta pantalla
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    }

}

@Composable
fun RowScope.MyOwnItem(navItem: NavItem<out Any>, isSelected: Boolean, onClick: () -> Unit) {
    NavigationBarItem(
        selected = isSelected, onClick = { onClick() }, icon = {
            Icon(
                imageVector = navItem.icon, contentDescription = navItem.name
            )
        }, label = {
            Text(navItem.name)
        }, alwaysShowLabel = true, colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onSurface,
            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            indicatorColor = MaterialTheme.colorScheme.surface,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            selectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
        )
    )

}