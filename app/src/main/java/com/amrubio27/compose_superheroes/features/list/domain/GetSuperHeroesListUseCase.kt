package com.amrubio27.compose_superheroes.features.list.domain

class GetSuperHeroesListUseCase(
    private val repository: SuperHeroesRepository
) {
    operator fun invoke(): Result<List<SuperHero>> {
        return repository.getSuperHeroes()
    }
}