package com.amrubio27.compose_superheroes.features.list.presentation.components.superHeroItem

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.amrubio27.compose_superheroes.R
import com.amrubio27.compose_superheroes.ui.theme.dimens

/**
 * Componente reutilizable que envuelve SuperheroItem con SwipeToDismissBox.
 * Sigue el principio de Single Responsibility: solo maneja el gesto de swipe.
 *
 * @param hero Modelo del superhéroe a mostrar
 * @param onDismiss Callback que se ejecuta cuando se completa el gesto de swipe
 * @param navigateToDetail Callback para navegar al detalle del superhéroe
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableSuperheroItem(
    hero: SuperHeroItemModel,
    onDismiss: (Int) -> Unit,
    navigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.EndToStart -> {
                    // El usuario deslizó de derecha a izquierda (borrar)
                    onDismiss(hero.id)
                    true
                }

                else -> false
            }
        }
    )

    // Animación de color del fondo según el progreso del swipe
    val backgroundColor by animateColorAsState(
        when (dismissState.targetValue) {
            SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
            else -> Color.Transparent
        },
        label = "background_color"
    )

    // Animación de escala del icono según el progreso del swipe
    val iconScale by animateFloatAsState(
        if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) 1.3f else 0.8f,
        label = "icon_scale"
    )

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        enableDismissFromStartToEnd = false, // Solo permitimos swipe de derecha a izquierda
        backgroundContent = {
            // Contenido del fondo que se muestra durante el swipe
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(horizontal = MaterialTheme.dimens.paddingLarge),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.content_description_delete_superhero),
                        tint = MaterialTheme.colorScheme.onError,
                        modifier = Modifier.scale(iconScale)
                    )
                }
            }
        }
    ) {
        // Contenido principal: el item del superhéroe
        SuperheroItem(
            hero = hero,
            navigateToDetail = navigateToDetail
        )
    }
}
