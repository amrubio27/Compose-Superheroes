package com.amrubio27.compose_superheroes.features.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrubio27.compose_superheroes.features.list.domain.DeleteSuperHeroUseCase
import com.amrubio27.compose_superheroes.features.list.domain.GetSuperHeroesListUseCase
import com.amrubio27.compose_superheroes.features.list.presentation.components.superHeroItem.SuperHeroItemModel
import com.amrubio27.compose_superheroes.features.list.presentation.components.superHeroItem.toItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SuperHeroesListViewModel(
    private val getSuperHeroesListUseCase: GetSuperHeroesListUseCase,
    private val deleteSuperHeroUseCase: DeleteSuperHeroUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SuperHeroesListUiState())
    val uiState: StateFlow<SuperHeroesListUiState> = _uiState

    companion object {
        private const val SNACKBAR_DURATION_MILLIS = 5000L
    }

    // Job para cancelar el borrado definitivo si el usuario deshace
    private var deletionJob: Job? = null

    fun fetchSuperHeroes() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = getSuperHeroesListUseCase()
            result.fold(
                onSuccess = { heroes ->
                    _uiState.update {
                        it.copy(
                            superHeroes = heroes.map { hero ->
                                hero.toItemModel()
                            },
                            isLoading = false,
                            error = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
            )
        }
    }

    /**
     * Borrado optimista: Oculta el héroe de la lista inmediatamente
     * y programa el borrado real tras el timeout del Snackbar.
     */
    fun deleteHeroOptimistic(heroId: Int) {
        // Guardamos el héroe que vamos a "borrar" por si hay que deshacerlo
        val heroToDelete = _uiState.value.superHeroes.find { it.id == heroId } ?: return

        // Guardamos la lista completa antes del borrado
        val originalList = _uiState.value.superHeroes

        // Actualizamos la UI quitando el héroe de la lista (borrado optimista)
        _uiState.update { currentState ->
            currentState.copy(
                superHeroes = currentState.superHeroes.filter { it.id != heroId },
                pendingDeletion = OptimisticDeleteState(
                    deletedHero = heroToDelete,
                    originalList = originalList
                )
            )
        }

        // Cancelamos cualquier borrado pendiente anterior
        deletionJob?.cancel()

        // Programamos el borrado real después de 5 segundos (duración del Snackbar)
        deletionJob = viewModelScope.launch(Dispatchers.IO) {
            delay(SNACKBAR_DURATION_MILLIS) // Tiempo para que el usuario pueda deshacer

            // Si llegamos aquí, el usuario NO deshizo, procedemos con el borrado real
            val result = deleteSuperHeroUseCase(heroId)

            result.fold(
                onSuccess = {
                    // Borrado exitoso, limpiamos el estado de pendingDeletion
                    _uiState.update { it.copy(pendingDeletion = null) }
                },
                onFailure = { error ->
                    // Error al borrar, restauramos el héroe y mostramos error
                    _uiState.update { currentState ->
                        currentState.copy(
                            superHeroes = originalList,
                            pendingDeletion = null,
                            error = "Error al borrar: ${error.message}"
                        )
                    }
                }
            )
        }
    }

    /**
     * Deshace el borrado optimista, restaurando el héroe a la lista.
     */
    fun undoDelete() {
        val pendingDeletion = _uiState.value.pendingDeletion ?: return

        // Cancelamos el job de borrado real
        deletionJob?.cancel()

        // Restauramos la lista original
        _uiState.update { currentState ->
            currentState.copy(
                superHeroes = pendingDeletion.originalList,
                pendingDeletion = null
            )
        }
    }

    /**
     * Limpia el mensaje de Snackbar (para cuando se descarta sin acción)
     */
    fun clearPendingDeletion() {
        _uiState.update { it.copy(pendingDeletion = null) }
    }
}

/**
 * Estado de un borrado optimista pendiente.
 * Contiene la información necesaria para deshacer la operación.
 */
data class OptimisticDeleteState(
    val deletedHero: SuperHeroItemModel,
    val originalList: List<SuperHeroItemModel>
)

data class SuperHeroesListUiState(
    val superHeroes: List<SuperHeroItemModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val pendingDeletion: OptimisticDeleteState? = null
)