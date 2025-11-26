package com.amrubio27.compose_superheroes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Define las dimensiones estándar de la aplicación.
 * Usar CompositionLocal permite cambiar estos valores dinámicamente
 * (por ejemplo, para tablets o diferentes configuraciones).
 */
data class Dimens(
    // Padding / Spacing
    val paddingExtraSmall: Dp = 4.dp,
    val paddingSmall: Dp = 8.dp,
    val paddingMedium: Dp = 16.dp,
    val paddingLarge: Dp = 24.dp,

    // Tamaños de componentes
    val listItemHeight: Dp = 48.dp,
    val iconSizeSmall: Dp = 24.dp,
    val iconSizeMedium: Dp = 48.dp,
    val errorImageSize: Dp = 120.dp,
    val heroImageHeight: Dp = 300.dp,

    // Elevaciones
    val elevationSmall: Dp = 4.dp,
    val elevationMedium: Dp = 8.dp
)

/**
 * CompositionLocal para proporcionar Dimens a través del árbol de composición.
 * Usar staticCompositionLocalOf porque los valores de dimensiones raramente cambian.
 */
val LocalDimens = staticCompositionLocalOf { Dimens() }

/**
 * Extension property para acceder a las dimensiones desde MaterialTheme.
 * Uso: MaterialTheme.dimens.paddingMedium
 */
val MaterialTheme.dimens: Dimens
    @Composable
    @ReadOnlyComposable
    get() = LocalDimens.current
