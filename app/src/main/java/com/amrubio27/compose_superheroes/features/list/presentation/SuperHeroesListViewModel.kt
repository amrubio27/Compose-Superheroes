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

    private val allSuperHeroes = MutableStateFlow<List<SuperHeroItemModel>>(emptyList())

    fun onSearchQueryChange(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                superHeroes = if (query.isEmpty()) {
                    allSuperHeroes.value
                } else {
                    allSuperHeroes.value.filter { hero ->
                        hero.name.contains(query, ignoreCase = true)
                    }
                }
            )
        }
    }

    fun fetchSuperHeroes() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = getSuperHeroesListUseCase()
            result.fold(
                onSuccess = { heroes ->
                    val heroModels = heroes.map { hero -> hero.toItemModel() }
                    allSuperHeroes.update { heroModels }
                    _uiState.update {
                        it.copy(
                            superHeroes = heroModels,
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
    val error: String? = null,
    val searchQuery: String = ""
)