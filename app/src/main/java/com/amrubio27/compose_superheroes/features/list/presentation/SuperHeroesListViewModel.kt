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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SuperHeroesListViewModel(
    private val getSuperHeroesListUseCase: GetSuperHeroesListUseCase,
    private val deleteSuperHeroUseCase: DeleteSuperHeroUseCase
) : ViewModel() {

    companion object {
        private const val SNACKBAR_DURATION_MILLIS = 5000L
    }

    private var deletionJob: Job? = null

    private val _allSuperHeroes = MutableStateFlow<List<SuperHeroItemModel>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)
    private val _pendingDeletion = MutableStateFlow<OptimisticDeleteState?>(null)
    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<SuperHeroesListUiState> = combine(
        _allSuperHeroes,
        _isLoading,
        _error,
        _pendingDeletion,
        _searchQuery
    ) { allHeroes, isLoading, error, pendingDeletion, searchQuery ->
        val filteredHeroes = if (searchQuery.isEmpty()) {
            allHeroes
        } else {
            allHeroes.filter { hero ->
                hero.name.contains(searchQuery, ignoreCase = true)
            }
        }

        // Apply optimistic deletion filter if needed
        val finalHeroes = if (pendingDeletion != null) {
            filteredHeroes.filter { it.id != pendingDeletion.deletedHero.id }
        } else {
            filteredHeroes
        }

        SuperHeroesListUiState(
            superHeroes = finalHeroes,
            isLoading = isLoading,
            error = error,
            pendingDeletion = pendingDeletion,
            searchQuery = searchQuery
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SuperHeroesListUiState()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun fetchSuperHeroes() {
        _isLoading.value = true
        _error.value = null
        
        viewModelScope.launch(Dispatchers.IO) {
            val result = getSuperHeroesListUseCase()
            result.fold(
                onSuccess = { heroes ->
                    val heroModels = heroes.map { hero -> hero.toItemModel() }
                    _allSuperHeroes.value = heroModels
                    _isLoading.value = false
                    _error.value = null
                },
                onFailure = { error ->
                    _isLoading.value = false
                    _error.value = error.message
                }
            )
        }
    }

    fun deleteHeroOptimistic(heroId: Int) {
        // Si ya hay un borrado pendiente, lo confirmamos inmediatamente antes de procesar el nuevo
        _pendingDeletion.value?.let { previousPending ->
            // 1. Confirmamos el borrado en la lista local (source of truth)
            _allSuperHeroes.update { currentList ->
                currentList.filter { it.id != previousPending.deletedHero.id }
            }
            // 2. Lanzamos el borrado real inmediatamente (fire and forget)
            viewModelScope.launch(Dispatchers.IO) {
                deleteSuperHeroUseCase(previousPending.deletedHero.id)
            }
        }

        // Guardamos el héroe que vamos a "borrar" por si hay que deshacerlo
        val heroToDelete = _allSuperHeroes.value.find { it.id == heroId } ?: return

        // Actualizamos el estado de borrado pendiente.
        _pendingDeletion.value = OptimisticDeleteState(
            deletedHero = heroToDelete
        )

        deletionJob?.cancel()

        // Programamos el borrado real después de 5 segundos (duración del Snackbar)
        deletionJob = viewModelScope.launch(Dispatchers.IO) {
            delay(SNACKBAR_DURATION_MILLIS)

            val result = deleteSuperHeroUseCase(heroId)

            result.fold(
                onSuccess = {
                    // Confirmamos el borrado eliminándolo de la lista maestra
                    _allSuperHeroes.update { currentList ->
                        currentList.filter { it.id != heroId }
                    }
                    _pendingDeletion.value = null
                },
                onFailure = { error ->
                    _pendingDeletion.value = null
                    _error.value = "Error al borrar: ${error.message}"
                }
            )
        }
    }

    fun undoDelete() {
        deletionJob?.cancel()
        _pendingDeletion.value = null
    }
}

/**
 * Estado de un borrado optimista pendiente.
 * Contiene la información necesaria para deshacer la operación.
 */
data class OptimisticDeleteState(
    val deletedHero: SuperHeroItemModel
)

data class SuperHeroesListUiState(
    val superHeroes: List<SuperHeroItemModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val pendingDeletion: OptimisticDeleteState? = null,
    val searchQuery: String = ""
)