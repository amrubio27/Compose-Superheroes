package com.amrubio27.compose_superheroes.features.detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.amrubio27.compose_superheroes.app.navigation.Detail
import com.amrubio27.compose_superheroes.features.list.domain.GetSuperHeroByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SuperHeroesDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getSuperHeroByIdUseCase: GetSuperHeroByIdUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SuperHeroesDetailUiState())
    val uiState: StateFlow<SuperHeroesDetailUiState> = _uiState

    init {
        val id = savedStateHandle.toRoute<Detail>().id
        fetchSuperHeroById(id)
    }

    private fun fetchSuperHeroById(id: Int) {
        _uiState.update {
            it.copy(isLoading = true, error = null)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val result = getSuperHeroByIdUseCase(id)
            result.fold(
                onSuccess = { hero ->
                    _uiState.update {
                        it.copy(
                            superHero = hero.toDetailUiModel(),
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

data class SuperHeroesDetailUiState(
    val superHero: SuperHeroDetailUiModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)