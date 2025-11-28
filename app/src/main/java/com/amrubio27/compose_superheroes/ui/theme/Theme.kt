package com.amrubio27.compose_superheroes.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlueDark,
    onPrimary = OnPrimaryBlueDark,
    primaryContainer = PrimaryContainerBlueDark,
    onPrimaryContainer = OnPrimaryContainerBlueDark,
    secondary = SecondaryRedDark,
    onSecondary = OnSecondaryRedDark,
    secondaryContainer = SecondaryContainerRedDark,
    onSecondaryContainer = OnSecondaryContainerRedDark,
    tertiary = TertiaryGoldDark,
    onTertiary = OnTertiaryGoldDark,
    tertiaryContainer = TertiaryContainerGoldDark,
    onTertiaryContainer = OnTertiaryContainerGoldDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    onBackground = Neutral90,
    onSurface = Neutral90
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = OnPrimaryWhite,
    primaryContainer = PrimaryContainerBlue,
    onPrimaryContainer = OnPrimaryContainerBlue,
    secondary = SecondaryRed,
    onSecondary = OnSecondaryWhite,
    secondaryContainer = SecondaryContainerRed,
    onSecondaryContainer = OnSecondaryContainerRed,
    tertiary = TertiaryGold,
    onTertiary = OnTertiaryBlack,
    tertiaryContainer = TertiaryContainerGold,
    onTertiaryContainer = OnTertiaryContainerGold,
    background = BackgroundLight,
    surface = SurfaceLight,
    onBackground = Neutral10,
    onSurface = Neutral10
)

/**
 * Constantes de alpha para el contenido.
 * Útiles para estados deshabilitados o con menor énfasis.
 */
object ContentAlpha {
    const val high = 0.9f
    const val medium = 0.74f
    const val disabled = 0.6f
    const val low = 0.38f
}

@Composable
fun ComposeSuperheroesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(LocalDimens provides Dimens()) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = AppShapes,
            content = content
        )
    }
}