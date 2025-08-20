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

    companion object {
        private const val UNDO_DELAY_MS = 4000L
    }
    
    private val _uiState = MutableStateFlow(SuperHeroesListUiState())
    val uiState: StateFlow<SuperHeroesListUiState> = _uiState

    private var undoJob: Job? = null

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
     * Deletes a superhero with undo functionality.
     */
    fun deleteHero(heroId: Int, heroName: String) {
        val heroToDelete = _uiState.value.superHeroes.find { it.id == heroId } ?: return

        undoJob?.cancel()

        // Remove hero from UI immediately
        _uiState.update { currentState ->
            currentState.copy(
                superHeroes = currentState.superHeroes.filterNot { it.id == heroId },
                pendingDeletion = PendingDeletion(heroName, heroToDelete)
            )
        }

        // Schedule actual deletion
        undoJob = viewModelScope.launch(Dispatchers.IO) {
            delay(UNDO_DELAY_MS)
            deleteSuperHeroUseCase(heroId).fold(
                onSuccess = {
                    _uiState.update { it.copy(pendingDeletion = null) }
                },
                onFailure = { error ->
                    restoreHero(heroToDelete, "Error al eliminar $heroName: ${error.message}")
                }
            )
        }
    }

    /**
     * Restores a deleted superhero.
     */
    fun undoDelete() {
        val pendingDeletion = _uiState.value.pendingDeletion ?: return

        undoJob?.cancel()

        // Find insertion position to maintain list order
        val currentHeroes = _uiState.value.superHeroes.toMutableList()
        val insertIndex = findInsertionIndex(currentHeroes, pendingDeletion.deletedHero)
        currentHeroes.add(insertIndex, pendingDeletion.deletedHero)

        _uiState.update { it.copy(superHeroes = currentHeroes, pendingDeletion = null) }
    }

    /**
     * Clears error messages and pending deletions.
     */
    fun clearMessages() {
        _uiState.update { it.copy(error = null, pendingDeletion = null) }
    }

    private fun restoreHero(hero: SuperHeroItemModel, errorMessage: String) {
        val currentHeroes = _uiState.value.superHeroes.toMutableList()
        val insertIndex = findInsertionIndex(currentHeroes, hero)
        currentHeroes.add(insertIndex, hero)

        _uiState.update {
            it.copy(superHeroes = currentHeroes, error = errorMessage, pendingDeletion = null)
        }
    }

    private fun findInsertionIndex(list: List<SuperHeroItemModel>, hero: SuperHeroItemModel): Int {
        return list.indexOfFirst { it.id > hero.id }.takeIf { it != -1 } ?: list.size
    }
}

data class SuperHeroesListUiState(
    val superHeroes: List<SuperHeroItemModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val pendingDeletion: PendingDeletion? = null
)

data class PendingDeletion(
    val heroName: String,
    val deletedHero: SuperHeroItemModel
)