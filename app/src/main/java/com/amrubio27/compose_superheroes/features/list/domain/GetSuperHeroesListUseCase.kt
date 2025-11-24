package com.amrubio27.compose_superheroes.features.list.domain

import org.koin.core.annotation.Single

@Single
class GetSuperHeroesListUseCase(
    private val repository: SuperHeroesRepository
) {
    suspend operator fun invoke(forceRefresh: Boolean = false): Result<List<SuperHero>> {
        return repository.getSuperHeroes(forceRefresh)
    }
}
