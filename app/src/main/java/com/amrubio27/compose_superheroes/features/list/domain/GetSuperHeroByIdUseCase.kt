package com.amrubio27.compose_superheroes.features.list.domain

import org.koin.core.annotation.Single

@Single
class GetSuperHeroByIdUseCase(
    private val repository: SuperHeroesRepository
) {
    suspend operator fun invoke(id: Int): Result<SuperHero> {
        return repository.getHeroById(id)
    }
}