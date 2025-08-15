package com.amrubio27.compose_superheroes.features.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrubio27.compose_superheroes.features.list.domain.GetSuperHeroesListUseCase
import com.amrubio27.compose_superheroes.features.list.presentation.components.superHeroItem.SuperHeroItemModel
import com.amrubio27.compose_superheroes.features.list.presentation.components.superHeroItem.toItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SuperHeroesListViewModel(
    private val getSuperHeroesListUseCase: GetSuperHeroesListUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SuperHeroesListUiState())
    val uiState: StateFlow<SuperHeroesListUiState> = _uiState

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
}

data class SuperHeroesListUiState(
    val superHeroes: List<SuperHeroItemModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)