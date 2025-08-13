package com.amrubio27.compose_superheroes.features.list.domain

import org.koin.core.annotation.Single

@Single
class GetSuperHeroesListUseCase(
    private val repository: SuperHeroesRepository
) {
    suspend operator fun invoke(): Result<List<SuperHero>> {
        return repository.getSuperHeroes()
    }
}