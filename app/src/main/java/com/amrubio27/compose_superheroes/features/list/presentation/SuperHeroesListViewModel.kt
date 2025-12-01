package com.amrubio27.compose_superheroes.features.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrubio27.compose_superheroes.app.domain.ErrorApp
import com.amrubio27.compose_superheroes.features.list.domain.DeleteSuperHeroUseCase
import com.amrubio27.compose_superheroes.features.list.domain.GetSuperHeroesListUseCase
import com.amrubio27.compose_superheroes.features.list.presentation.components.superHeroItem.SuperHeroItemModel
import com.amrubio27.compose_superheroes.features.list.presentation.components.superHeroItem.toItemModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
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

    // --- INPUTS (Fuentes de Verdad) ---
    private val _allSuperHeroes = MutableStateFlow<List<SuperHeroItemModel>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _pendingDeletion = MutableStateFlow<OptimisticDeleteState?>(null)

    // Flags de UI aislados para evitar recomposiciones masivas
    private val _uiFlags = MutableStateFlow(UiFlags())

    // --- DERIVED STATE (Lógica Pura) ---
    // Extraemos la lógica de filtrado para que el combine sea legible
    // --- DERIVED STATE (Lógica Pura) ---
    // Extraemos la lógica de filtrado para que el combine sea legible
    private val _filteredHeroesFlow = combine(
        _allSuperHeroes,
        _searchQuery.debounce(300L),
        _pendingDeletion
    ) { heroes, query, pending ->
        applyFilters(heroes, query, pending)
    }

    // --- UI STATE (Salida Pública) ---
    // CRÍTICO: Añadimos _searchQuery y _pendingDeletion aquí también.
    // Aunque _filteredHeroesFlow ya depende de ellos, si usas .value dentro del lambda
    // te arriesgas a "glitches" (milisegundos donde la lista está filtrada pero el texto del searchbox es viejo).

    val uiState: StateFlow<SuperHeroesListUiState> = combine(
        _filteredHeroesFlow,
        _uiFlags,
        _searchQuery,
        _pendingDeletion
    ) { filteredList, flags, query, pending ->
        SuperHeroesListUiState(
            superHeroes = filteredList,
            isLoading = flags.isLoading,
            isRefreshing = flags.isRefreshing,
            error = flags.error,
            pendingDeletion = pending,
            searchQuery = query
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SuperHeroesListUiState(isLoading = true)
    )

    init {
        fetchSuperHeroes()
    }

    // --- ACCIONES ---

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun fetchSuperHeroes() {
        loadSuperHeroes(isRefresh = false)
    }

    fun refreshSuperHeroes() {
        loadSuperHeroes(isRefresh = true)
    }

    private fun loadSuperHeroes(isRefresh: Boolean) {
        _uiFlags.update {
            if (isRefresh) {
                it.copy(isRefreshing = true, error = null)
            } else {
                it.copy(isLoading = true, error = null)
            }
        }

        viewModelScope.launch {
            getSuperHeroesListUseCase(forceRefresh = isRefresh).fold(onSuccess = { heroes ->
                _allSuperHeroes.value = heroes.map { it.toItemModel() }
                _uiFlags.update {
                    if (isRefresh) {
                        it.copy(isRefreshing = false)
                    } else {
                        it.copy(isLoading = false)
                    }
                }
            }, onFailure = { error ->
                _uiFlags.update {
                    if (isRefresh) {
                        it.copy(isRefreshing = false, error = error as? ErrorApp)
                    } else {
                        it.copy(isLoading = false, error = error as? ErrorApp)
                    }
                }
            })
        }
    }

    fun deleteHeroOptimistic(heroId: Int) {
        // 1. Gestión de borrados rápidos consecutivos (Fast Deletion)
        // Si el usuario borra el Item A y, antes de 5s, borra el Item B:
        // Debemos confirmar el borrado de A inmediatamente antes de procesar B.
        _pendingDeletion.value?.let { previousPending ->
            if (previousPending.deletedHero.id != heroId) {
                commitDeletion(previousPending.deletedHero.id)
            }
        }

        // 2. Preparar nuevo borrado optimista
        val heroToDelete = _allSuperHeroes.value.find { it.id == heroId } ?: return

        // Actualizamos estado visual (el combine filtrará la lista automáticamente)
        _pendingDeletion.value = OptimisticDeleteState(heroToDelete)

        // Cancelamos el job anterior (el de espera), porque ya lo hemos commiteado arriba manualmente
        deletionJob?.cancel()

        // 3. Programar el borrado real (Delayed)
        deletionJob = viewModelScope.launch {
            delay(SNACKBAR_DURATION_MILLIS)
            commitDeletion(heroId)
        }
    }

    fun undoDelete() {
        // Al cancelar el job, el commitDeletion nunca ocurre.
        deletionJob?.cancel()
        // Al poner esto a null, el combine vuelve a mostrar el ítem original de _allSuperHeroes
        _pendingDeletion.value = null
    }

    fun clearError() {
        _uiFlags.update { it.copy(error = null) }
    }

    // --- HELPER FUNCTIONS ---

    // Función privada para consolidar la lógica de "Hacer efectivo el borrado"
    private fun commitDeletion(heroId: Int) {
        viewModelScope.launch { // Lanzamos corrutina para la operación de red
            val result = deleteSuperHeroUseCase(heroId)

            result.fold(onSuccess = {
                // CRÍTICO: Race Condition Fix
                // Solo limpiamos el pendingDeletion si coincide con el que acabamos de borrar.
                // Si el usuario borró otro item mientras esto se procesaba, no queremos quitarle el snackbar del nuevo.
                _pendingDeletion.update { current ->
                    if (current?.deletedHero?.id == heroId) null else current
                }

                // Eliminamos definitivamente de la lista maestra
                _allSuperHeroes.update { list -> list.filter { it.id != heroId } }
            }, onFailure = { error ->
                // Si falla, restauramos la vista (quitamos el pending) y mostramos error
                _pendingDeletion.update { current ->
                    if (current?.deletedHero?.id == heroId) null else current
                }
                _uiFlags.update { it.copy(error = error as? ErrorApp) }
            })
        }
    }

    // Función pura de filtrado (Testing friendly)
    private fun applyFilters(
        heroes: List<SuperHeroItemModel>,
        query: String,
        pending: OptimisticDeleteState?
    ): List<SuperHeroItemModel> {
        return heroes.filter { it.name.contains(query, ignoreCase = true) }.let { list ->
            if (pending != null) list.filter { it.id != pending.deletedHero.id } else list
        }
    }

    // Flags UI Helper
    private data class UiFlags(
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,
        val error: ErrorApp? = null
    )
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
    val isRefreshing: Boolean = false,
    val error: ErrorApp? = null,
    val pendingDeletion: OptimisticDeleteState? = null,
    val searchQuery: String = ""
)
